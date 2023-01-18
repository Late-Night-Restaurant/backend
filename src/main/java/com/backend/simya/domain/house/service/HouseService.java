package com.backend.simya.domain.house.service;


import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
import com.backend.simya.domain.house.dto.request.HouseRequestDto;
import com.backend.simya.domain.house.dto.request.HouseUpdateRequestDto;
import com.backend.simya.domain.house.dto.response.HouseIntroductionResponseDto;
import com.backend.simya.domain.house.dto.response.HouseResponseDto;
import com.backend.simya.domain.house.dto.response.HouseShowResponseDto;
import com.backend.simya.domain.house.entity.Category;
import com.backend.simya.domain.house.entity.House;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.backend.simya.global.common.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final ProfileService profileService;
    private final TopicService topicService;
    private final ReviewService reviewService;

    @Transactional
    public HouseResponseDto createHouse(HouseRequestDto houseRequestDto) throws BaseException {
        try {
            Profile profile = profileService.findProfile(houseRequestDto.getProfileId());  // 해당하는 프로필 찾기
            House savedHouse = houseRepository.save(houseRequestDto.toEntity(profile));

            return HouseResponseDto.builder()
                    .houseId(savedHouse.getHouseId())
                    .category(savedHouse.getCategory().toString())
                    .houseName(savedHouse.getHouseName())
                    .comment(savedHouse.getComment())
                    .signboardImageUrl(savedHouse.getSignboardImageUrl())
                    .build();
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void openHouse(User loginUser, HouseOpenRequestDto houseOpenRequestDto) throws BaseException {
        House house = getHouse(houseOpenRequestDto.getHouseId());
        if (!loginUser.getUserId().equals(house.getProfile().getProfileId())) { // 이야기 집 생성자가 오픈하려는지 확인
            throw new BaseException(FAILED_TO_OPEN);
        }
        if (house.isOpen()) {  // 이미 오픈된 이야기 집인 경우
            throw new BaseException(HOUSE_ALREADY_OPENED);
        }

        try {
            house.openHouse(houseOpenRequestDto.getCapacity());

            topicService.createTopic(house, houseOpenRequestDto);
            log.info("{} 이야기 집이 오픈되었습니다.", house.getHouseName());
        } catch (Exception ignored) {
            throw new BaseException(HOUSE_OPEN_FAILED);
        }

    }

    public House getHouse(Long houseId) throws BaseException {
        return houseRepository.findById(houseId).orElseThrow(
                () -> new BaseException(HOUSE_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public HouseIntroductionResponseDto showHouse(Long houseId) throws BaseException {
        try {
            House house = getHouse(houseId);
            Profile profileInfo = profileService.findProfile(house.getProfile().getProfileId());
            List<Review> reviewList = reviewService.getReviewList(house);
            return HouseIntroductionResponseDto.from(profileInfo, house, reviewList);

        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void updateMain(User loginUser, HouseUpdateRequestDto houseUpdateRequestDto) throws BaseException {
        House house = getHouse(houseUpdateRequestDto.getHouseId());
        if (!loginUser.getUserId().equals(house.getProfile().getProfileId())) {  // 이야기 집 생성자가 수정하려는지 확인
            throw new BaseException(FAILED_TO_UPDATE);
        }
        try {
            house.update(houseUpdateRequestDto);
            log.info("{}의 이야기 집 간판이 수정되었습니다.", house.getHouseName());

        } catch (Exception ignored) {
            throw new BaseException(HOUSE_UPDATE_FAILED);
        }
    }

    @Transactional
    public void closeHouseRoom(User loginUser, Long houseId) throws BaseException {
        House house = getHouse(houseId);
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

    @Transactional
    public void updateMainMenu(Long houseId, User user, String menu) throws BaseException{
        House house = getHouse(houseId);

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
