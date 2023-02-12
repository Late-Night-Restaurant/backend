package com.backend.simya.domain.house.controller;

import com.backend.simya.domain.chat.dto.ChatRoomProfile;
import com.backend.simya.domain.chat.repository.ChatRoomRepository;
import com.backend.simya.domain.chat.service.ChatService;
import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
import com.backend.simya.domain.house.dto.request.HouseUpdateRequestDto;
import com.backend.simya.domain.house.dto.request.HouseCreateRequestDto;
import com.backend.simya.domain.house.dto.request.TopicRequestDto;
import com.backend.simya.domain.house.dto.response.*;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.entity.Topic;
import com.backend.simya.domain.house.service.HouseService;
import com.backend.simya.domain.house.service.TopicService;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.service.ProfileService;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import com.backend.simya.global.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;


    @GetMapping("")
    public BaseResponse<List<HouseSignboardResponseDto>> getAllHouseSignboards() {
        try {
            return new BaseResponse<>(houseService.getAllHouseSignboard());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("")
    public BaseResponse<HouseResponseDto> createHouse(@RequestPart HouseCreateRequestDto houseCreateRequestDto,
                                                      @RequestPart(value = "image", required = false) MultipartFile signboardImage) {
        try {
            Profile masterProfile = profileService.findProfile(houseCreateRequestDto.getProfileId());
            return new BaseResponse<>(houseService.createHouse(houseCreateRequestDto, signboardImage, masterProfile));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("{houseId}")
    public BaseResponse<HouseIntroductionResponseDto> showHouseIntroduction(@PathVariable("houseId") Long houseId) {
        try {
            return new BaseResponse<>(houseService.getHouseIntroduction(houseId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/open")
    public BaseResponse<HouseSignboardResponseDto> openHouse(@RequestBody HouseOpenRequestDto houseOpenRequestDto) {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            chatService.openChatRoom(houseOpenRequestDto);
            return new BaseResponse<>(houseService.openHouse(loginUser, houseOpenRequestDto));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/close/{houseId}")
    public BaseResponse<BaseResponseStatus> closeHouse(@PathVariable("houseId") Long houseId) {
        try {
            houseService.closeHouse(houseId);
            chatService.closeChatRoom(houseId);
            return new BaseResponse<>(SUCCESS_TO_CLOSE_HOUSE);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{houseId}")
    public BaseResponse<BaseResponseStatus> deleteHouse(@PathVariable("houseId") Long houseId) {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            houseService.deleteHouse(loginUser, houseId);
            return new BaseResponse<>(SUCCESS_TO_DELETE_HOUSE);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{houseId}/category")
    public BaseResponse<BaseResponseStatus> updateCategory(@PathVariable("houseId") Long houseId, @RequestParam("category") String category) {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            houseService.updateCategory(houseId, loginUser, category);
            return new BaseResponse<>(SUCCESS_TO_UPDATE_CATEGORY);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{houseId}/signboard")
    public BaseResponse<HouseResponseDto> updateSignboard(@PathVariable("houseId") Long houseId,
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
            List<HouseResponseDto> houseResponseDtoList = houseService.getMyHouses(currentUserId);
            if (houseResponseDtoList.isEmpty()) {
                return new BaseResponse<>(NO_HOUSE_YET);
            } else {
                return new BaseResponse<>(houseResponseDtoList);
            }
        } catch (Exception e) {
            return new BaseResponse<>(DATABASE_ERROR);
        }
    }

    @GetMapping("/{houseId}/topic")
    public BaseResponse<List<TopicResponseDto>> showHousesAllTopic(@PathVariable("houseId") Long houseId) {
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

    @GetMapping("/{houseId}/guest")
    public BaseResponse<List<ChatRoomProfile>> getCurrentGuestsProfile(@PathVariable("houseId") Long houseId) {
        return new BaseResponse<>(chatRoomRepository.getRoomProfileList(String.valueOf(houseId)));
    }


    @PostMapping("/{houseId}/topic")
    public BaseResponse<TopicResponseDto> registerNewTopic(@PathVariable("houseId") Long houseId,
                                                           @RequestBody TopicRequestDto topicRequestDto) {
        try {
            House houseToRegisterTopic = houseService.findHouse(houseId);
            Topic newTopicToRegister = topicRequestDto.toEntity(houseToRegisterTopic, false);
            return new BaseResponse<>(houseService.registerNewTopic(houseToRegisterTopic, newTopicToRegister));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{houseId}/topic/{topicId}")
    public BaseResponse<BaseResponseStatus> deleteTopic(@PathVariable("houseId") Long houseId,
                                                        @PathVariable("topicId") Long topicId) {
        try{
            Topic topicToDelete = topicService.findTopic(topicId);
            houseService.findHouse(houseId).deleteTopic(topicToDelete);
            return new BaseResponse<>(SUCCESS_TO_DELETE_TOPIC);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
