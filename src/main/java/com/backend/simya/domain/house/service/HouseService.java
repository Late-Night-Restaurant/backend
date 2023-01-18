package com.backend.simya.domain.house.service;


import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
import com.backend.simya.domain.house.dto.request.HouseUpdateRequestDto;
import com.backend.simya.domain.house.dto.request.NewHouseRequestDto;
import com.backend.simya.domain.house.dto.request.TopicRequestDto;
import com.backend.simya.domain.house.dto.response.HouseIntroductionResponseDto;
import com.backend.simya.domain.house.dto.response.HouseResponseDto;
import com.backend.simya.domain.house.dto.response.HouseSignboardResponseDto;
import com.backend.simya.domain.house.dto.response.TopicResponseDto;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.entity.Topic;
import com.backend.simya.domain.house.repository.HouseRepository;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.service.ProfileService;
import com.backend.simya.domain.review.dto.ReviewResponseDto;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.review.service.ReviewService;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.backend.simya.global.common.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final ProfileService profileService;
    private final TopicService topicService;
    private final ReviewService reviewService;


    // 새 이야기 집 생성
    @Transactional
    public HouseResponseDto createHouse(NewHouseRequestDto newHouseRequestDto) throws BaseException {
        try {
            Profile findProfile = profileService.findProfile(newHouseRequestDto.getProfileId());  // 해당하는 프로필 찾기
            House savedHouse = houseRepository.save(newHouseRequestDto.toEntity(findProfile));
            return HouseResponseDto.from(savedHouse);
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 이야기 집 오픈
    @Transactional
    public HouseSignboardResponseDto openHouse(User loginUser, HouseOpenRequestDto houseOpenRequestDto) throws BaseException {
        House houseToOpen = findHouse(houseOpenRequestDto.getHouseId());
        if (!loginUser.getUserId().equals(houseToOpen.getProfile().getProfileId())) { // 이야기 집 생성자가 오픈하려는지 확인
            throw new BaseException(FAILED_TO_OPEN);
        } else if (houseToOpen.isOpen()) {  // 이미 오픈된 이야기 집인 경우
            throw new BaseException(HOUSE_ALREADY_OPENED);
        } else {
            try {
                houseToOpen.openHouse(houseOpenRequestDto.getCapacity());
                return HouseSignboardResponseDto.from(houseToOpen,registerTopic(houseToOpen, houseOpenRequestDto.getTopic()).getTitle()) ;
            } catch (Exception ignored) {
                throw new BaseException(HOUSE_OPEN_FAILED);
            }
        }
    }

    //
    public House findHouse(Long houseId) throws BaseException {
        return houseRepository.findById(houseId).orElseThrow(
                () -> new BaseException(HOUSE_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public HouseIntroductionResponseDto showHouse(Long houseId) throws BaseException {
        try {
            House house = findHouse(houseId);
            Profile profileInfo = profileService.findProfile(house.getProfile().getProfileId());
            List<Review> reviewList = reviewService.getReviewList(house);
            return HouseIntroductionResponseDto.from(profileInfo, house, reviewList);

        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void updateMain(User loginUser, HouseUpdateRequestDto houseUpdateRequestDto) throws BaseException {
        House findHouse = findHouse(houseUpdateRequestDto.getHouseId());
        if (!loginUser.getUserId().equals(findHouse.getProfile().getProfileId())) {  // 이야기 집 생성자가 수정하려는지 확인
            throw new BaseException(FAILED_TO_UPDATE);
        }
        try {
            findHouse.update(houseUpdateRequestDto);
            log.info("{}의 이야기 집 간판이 수정되었습니다.", findHouse.getHouseName());

        } catch (Exception ignored) {
            throw new BaseException(HOUSE_UPDATE_FAILED);
        }
    }

    @Transactional
    public void closeHouseRoom(User loginUser, Long houseId) throws BaseException {
        House house = findHouse(houseId);
        if (!loginUser.getUserId().equals(house.getProfile().getProfileId())) {
            throw new BaseException(FAILED_TO_CLOSE);
        }

        try {
            house.delete();
            log.info("{}의 이야기 집이 폐점되었습니다.", house.getHouseName());

            topicService.deleteAllTopic(house.getHouseId());  // 해당 이야기집의 topic 모두 삭제

        }catch (Exception ignored) {
            throw new BaseException(FAILED_TO_OPEN_HOUSE);
        }

    }

    public List<HouseSignboardResponseDto> findAllHouseSignBoard() throws BaseException{

        List<House> OpenedHouses = houseRepository.findAll().stream()
                .filter(House::isActivated)
                .filter(House::isOpen)
                .collect(Collectors.toList());

        if(OpenedHouses.isEmpty()){
            throw new BaseException(NO_HOUSE_YET);
        } else {
            return OpenedHouses.stream()
                    .map(house -> HouseSignboardResponseDto.from(house, house.getTopicList().stream()
                            .filter(Topic::isTodayTopic)
                            .findFirst()
                            .get()
                            .getTitle()))
                    .collect(Collectors.toList());
        }
    }

    public List<HouseResponseDto> findMyHouses(Long currentUserId) {

        return houseRepository.findMyHousesByUserId(currentUserId).stream()
                .filter(House::isActivated)
                .filter(House::isOpen)
                .map(HouseResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<TopicResponseDto> findHousesTopic(Long houseId) {

        return topicService.findAllTopic(houseId).stream()
                .filter(Topic::isActivated)
                .map(TopicResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TopicResponseDto registerTopic(House houseToRegisterTopic, TopicRequestDto topicToRegisterDto) throws BaseException {
        Topic newTopic = topicService.createTopic(houseToRegisterTopic, topicToRegisterDto);
        houseToRegisterTopic.addTopic(newTopic);
        return TopicResponseDto.from(newTopic);
    }
    @Transactional
    public void updateMainMenu(Long houseId, User user, String menu) throws BaseException{
        House house = findHouse(houseId);

        if(!house.getProfile().getProfileId().equals(user.getUserId())) {
            throw new BaseException(FAILED_TO_UPDATE_MENU);
        }

        try {
            house.updateMenu(menu);
            log.info("이야기집의 전문메뉴가 {}로 수정되었습니다.", menu);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_UPDATE_MAIN_MENU);
        }


    }


}
