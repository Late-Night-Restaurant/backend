package com.backend.simya.domain.profile.controller;

import com.backend.simya.domain.profile.dto.request.ProfileRequestDto;
import com.backend.simya.domain.profile.dto.request.ProfileUpdateDto;
import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.service.ProfileService;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.backend.simya.global.common.BaseResponseStatus.USERS_NOT_AUTHORIZED;

@Slf4j
@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    //    private final AuthService authService;
    private final UserService userService;

    @PostMapping("")
    public BaseResponse<ProfileResponseDto> createProfile(@Valid @RequestBody ProfileRequestDto profileRequestDto) throws BaseException {

//        User user = authService.authenticateUser();  // 현재 접속한 유저
        User user = userService.getMyUserWithAuthorities().orElseThrow(
                () -> new BaseException(USERS_NOT_AUTHORIZED)
        );
        log.info("ProfileController - user: {}", user);
        if (user == null) {
            return new BaseResponse("존재하지 않는 사용자입니다.");   // TODO Custom Status ENUM 으로 만들어서 관리
        }

        try {
            profileRequestDto.setUserProfile(user);
            ProfileResponseDto profileResponseDto = profileService.createProfile(profileRequestDto);

            return new BaseResponse<>(profileResponseDto);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/{profileId}")
    public BaseResponse<String> updateProfile(@PathVariable("profileId") Long profileId, @RequestBody ProfileUpdateDto profileUpdateDto) {

        try {
            profileUpdateDto.setProfileId(profileId);
            profileService.updateProfile(profileUpdateDto);

            String result = "프로필 수정이 완료되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/{profileId}/delete")
    public BaseResponse<String> deleteProfile(@PathVariable("profileId") Long profileId) {

        try {
            profileService.deleteProfile(profileId);
            String result = "프로필이 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/{profileId}/main")
    public BaseResponse<String> setMainProfile(@PathVariable("profileId") Long profileId) {
        try {
            profileService.setMainProfile(profileId);
            String result = "메인 프로필이 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/{profileId}")
    public BaseResponse<Profile> getProfileInfo(@PathVariable("profileId") Long profileId) {
        try {
            return new BaseResponse<>(profileService.getProfileInfo(profileId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
