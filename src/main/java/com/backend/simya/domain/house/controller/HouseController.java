package com.backend.simya.domain.house.controller;

import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
import com.backend.simya.domain.house.dto.request.HouseUpdateRequestDto;
import com.backend.simya.domain.house.dto.request.NewHouseRequestDto;
import com.backend.simya.domain.house.dto.request.TopicRequestDto;
import com.backend.simya.domain.house.dto.response.*;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.entity.Topic;
import com.backend.simya.domain.house.service.HouseService;
import com.backend.simya.domain.house.service.TopicService;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.service.ProfileService;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.review.service.ReviewService;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import com.backend.simya.global.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.backend.simya.global.common.BaseResponseStatus.*;

@RestController
@RequestMapping("/simya/house")
@RequiredArgsConstructor
@Slf4j
public class HouseController {

    private final HouseService houseService;
    private final UserService userService;
    private final TopicService topicService;
    private final ProfileService profileService;
    private final ReviewService reviewService;


    @GetMapping("")
    public BaseResponse<List<HouseSignboardResponseDto>> getAllHouseSignboards() {
        try {
            return new BaseResponse<>(houseService.getAllHouseSignboard());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("")
    public BaseResponse<HouseResponseDto> createHouse(@RequestBody NewHouseRequestDto newHouseRequestDto) {
        try {
            Profile masterProfile = profileService.findProfile(newHouseRequestDto.getProfileId());  // 해당하는 프로필 찾기
            return new BaseResponse<>(houseService.createHouse(masterProfile, newHouseRequestDto));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("{house-id}")
    public BaseResponse<HouseIntroductionResponseDto> showHouseIntroduction(@PathVariable("house-id") Long houseId) {
        try {
            House findHouse = houseService.findHouse(houseId);
            Profile masterProfile = profileService.findProfile(findHouse.getProfile().getProfileId());
            List<Review> reviewList = reviewService.getReviewList(findHouse);
            return new BaseResponse<>(houseService.getHouseIntroduction(findHouse, masterProfile, reviewList));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/open")
    public BaseResponse<HouseSignboardResponseDto> openHouse(@RequestBody HouseOpenRequestDto houseOpenRequestDto) {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            return new BaseResponse<>(houseService.openHouse(loginUser, houseOpenRequestDto));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/close/{house-id}")
    public BaseResponse<BaseResponseStatus> closeHouse(@PathVariable("house-id") Long houseId) {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            houseService.closeHouse(houseId);
            return new BaseResponse<>(SUCCESS_TO_CLOSE_HOUSE);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/delete/{house-id}")
    public BaseResponse<String> deleteHouse(@PathVariable("house-id") Long houseId) {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            houseService.deleteHouse(loginUser, houseId);
            return new BaseResponse<>(SUCCESS_TO_DELETE_HOUSE);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{house-id}/signboard")
    public BaseResponse<HouseResponseDto> updateSignboard(@PathVariable("house-id") Long houseId,
                                                          @RequestBody HouseUpdateRequestDto houseUpdateRequestDto) {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            return new BaseResponse<>(SUCCESS_TO_UPDATE_HOUSE_SIGNBOARD, houseService.updateSignboard(loginUser, houseId, houseUpdateRequestDto));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/my-houses")
    public BaseResponse<List<HouseResponseDto>> getMyHouses() {
        try {
            Long currentUserId = userService.getMyUserWithAuthorities().getUserId();
            List<HouseResponseDto> houseSignboardResponseDtoList = houseService.getMyHouses(currentUserId);
            if (houseSignboardResponseDtoList.isEmpty()) {
                return new BaseResponse<>(NO_HOUSE_YET);
            } else {
                return new BaseResponse<>(houseSignboardResponseDtoList);
            }
        } catch (Exception e) {
            return new BaseResponse<>(DATABASE_ERROR);
        }
    }

    @GetMapping("/{house-id}/topic")
    public BaseResponse<List<TopicResponseDto>> showHousesAllTopic(@PathVariable("house-id") Long houseId) {
        try {
            List<TopicResponseDto> housesAllTopicResponseDtoList = houseService.getHousesTopic(houseId);
            if (housesAllTopicResponseDtoList.isEmpty()) {
                return new BaseResponse<>(FAILED_TO_LOAD_TODAY_TOPIC);
            } else {
                return new BaseResponse<>(housesAllTopicResponseDtoList);
            }
        } catch (Exception e) {
            return new BaseResponse<>(DATABASE_ERROR);
        }
    }

    @PostMapping("/{house-id}/topic")
    public BaseResponse<TopicResponseDto> registerNewTopic(@PathVariable("house-id") Long houseId,
                                                           @RequestBody TopicRequestDto topicRequestDto) {
        try {
            House houseToRegisterTopic = houseService.findHouse(houseId);
            Topic newTopicToRegister = topicRequestDto.toEntity(houseToRegisterTopic, false);
            return new BaseResponse<>(houseService.registerNewTopic(houseToRegisterTopic, newTopicToRegister));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{house-id}/topic/{topic-id}/delete")
    public BaseResponse<BaseResponseStatus> deleteTopic(@PathVariable("house-id") Long houseId,
                                                        @PathVariable("topic-id") Long topicId) {
        try{
            Topic topicToDelete = topicService.findTopic(topicId);
            houseService.findHouse(houseId).deleteTopic(topicToDelete);
            return new BaseResponse<>(SUCCESS_TO_DELETE_TOPIC);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

}
