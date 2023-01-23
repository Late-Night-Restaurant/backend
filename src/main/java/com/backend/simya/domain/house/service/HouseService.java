package com.backend.simya.domain.house.service;


import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
import com.backend.simya.domain.house.dto.request.HouseUpdateRequestDto;
import com.backend.simya.domain.house.dto.request.NewHouseRequestDto;
import com.backend.simya.domain.house.dto.request.TopicRequestDto;
import com.backend.simya.domain.house.dto.response.HouseIntroductionResponseDto;
import com.backend.simya.domain.house.dto.response.HouseResponseDto;
import com.backend.simya.domain.house.dto.response.HouseSignboardResponseDto;
import com.backend.simya.domain.house.dto.response.TopicResponseDto;
import com.backend.simya.domain.house.entity.Category;
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
import com.backend.simya.global.common.BaseResponse;
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
    private final TopicService topicService;


    // 새 이야기 집 생성
    @Transactional
    public HouseResponseDto createHouse(Profile masterProfile, NewHouseRequestDto newHouseRequestDto) throws BaseException {
        try {
            House savedHouse = houseRepository.save(newHouseRequestDto.toEntity(masterProfile));
            return HouseResponseDto.from(savedHouse);
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 이야기 집 오픈
    @Transactional
    public HouseSignboardResponseDto openHouse(User loginUser, HouseOpenRequestDto houseOpenRequestDto) throws BaseException {
        House houseToOpen = findHouse(houseOpenRequestDto.getHouseId());
        Topic todayTopic = houseOpenRequestDto.getTopic().toEntity(houseToOpen, true);
        if (!loginUser.getUserId().equals(houseToOpen.getProfile().getProfileId())) { // 이야기 집 생성자가 오픈하려는지 확인
            throw new BaseException(FAILED_TO_OPEN);
        } else if (houseToOpen.isOpen()) {  // 이미 오픈된 이야기 집인 경우
            throw new BaseException(HOUSE_ALREADY_OPENED);
        } else {
            try {
                houseToOpen.openHouse(houseOpenRequestDto.getCapacity());
                return HouseSignboardResponseDto.from(houseToOpen,registerNewTopic(houseToOpen, todayTopic).getTitle()) ;
            } catch (Exception ignored) {
                throw new BaseException(HOUSE_OPEN_FAILED);
            }
        }
    }

    public House findHouse(Long houseId) throws BaseException {
        return houseRepository.findById(houseId).orElseThrow(
                () -> new BaseException(FAILED_TO_FIND_HOUSE)
        );
    }

    @Transactional(readOnly = true)
    public HouseIntroductionResponseDto getHouseIntroduction(Long houseId) throws BaseException {
        try {
            House findHouse = findHouse(houseId);
            return HouseIntroductionResponseDto.from(findHouse);
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public HouseResponseDto updateSignboard(User loginUser, Long houseId, HouseUpdateRequestDto houseUpdateRequestDto) throws BaseException {
        House findHouse = findHouse(houseId);
        if (!loginUser.getUserId().equals(findHouse.getProfile().getProfileId())) {  // 이야기 집 생성자가 수정하려는지 확인
            throw new BaseException(FAILED_TO_UPDATE);
        }
        try {
            return HouseResponseDto.from(findHouse.updateSignboard(houseUpdateRequestDto));
        } catch (Exception ignored) {
            throw new BaseException(HOUSE_UPDATE_FAILED);
        }
    }

    @Transactional
    public void deleteHouse(User loginUser, Long houseId) throws BaseException {
        House houseToDelete = findHouse(houseId);
        if (!loginUser.getUserId().equals(houseToDelete.getProfile().getProfileId())) {
            throw new BaseException(FAILED_TO_CLOSE);
        } else {
            houseRepository.delete(houseToDelete);
        }
    }

    public List<HouseSignboardResponseDto> getAllHouseSignboard() throws BaseException {

        List<House> allHouse = houseRepository.findAll();
        if(allHouse.isEmpty()){
           throw new BaseException(NO_HOUSE_YET);
        } else {
            List<House> openedHouses = allHouse.stream()
                    .filter(House::isOpen)
                    .collect(Collectors.toList());
            if (openedHouses.isEmpty()) {
                throw new BaseException(NO_OPENED_HOUSE_YET);
            } else {
                return openedHouses.stream()
                        .map(house -> HouseSignboardResponseDto.from(house, house.getTopicList().stream()
                                .filter(Topic::isTodayTopic)
                                .findFirst()
                                .get()
                                .getTitle()))
                        .collect(Collectors.toList());
            }
        }
    }

    public List<HouseResponseDto> getMyHouses(Long currentUserId) {

        return houseRepository.findMyHousesByUserId(currentUserId).stream()
                .filter(House::isOpen)
                .map(HouseResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<TopicResponseDto> getHousesTopic(Long houseId) {

        return topicService.findAllTopic(houseId).stream()
                .map(TopicResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TopicResponseDto registerNewTopic(House houseToRegisterTopic, Topic topicToRegister) throws BaseException {
        houseToRegisterTopic.addTopic(topicToRegister);
        Topic newTopic = topicService.createTopic(topicToRegister);
        return TopicResponseDto.from(newTopic);
    }

    @Transactional
    public void updateCategory(Long houseId, User loginUser, String category) throws BaseException{
        House houseToUpdateCategory = findHouse(houseId);
        if (!houseToUpdateCategory.getProfile().getProfileId().equals(loginUser.getUserId())) {
            throw new BaseException(ONLY_MASTER_CAN_UPDATE);
        } else {
            if (houseToUpdateCategory.getCategory().equals(Category.nameOf(category))) {
                throw new BaseException(ALREADY_CATEGORY);
            } else {
                try {
                    houseToUpdateCategory.updateCategory(category);
                    log.info("이야기집의 전문메뉴가 {}로 수정되었습니다.", category);
                } catch (Exception ignored) {
                    throw new BaseException(DATABASE_ERROR);
                }
            }
        }
    }

    @Transactional
    public void closeHouse(Long houseId) throws BaseException {
        House houseToClose = findHouse(houseId);
        houseToClose.closeHouse();
        deleteAllTopicInHouse(houseToClose);
    }

    @Transactional
    public void deleteTopicInHouse(House myHouse, Topic topicToDelete) {
        myHouse.deleteTopic(topicToDelete);
    }

    @Transactional
    public void deleteAllTopicInHouse(House myHouse) {
        myHouse.deleteAllTopic();
    }
}
