package com.backend.simya.domain.user.service;


import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.repository.ProfileRepository;
import com.backend.simya.domain.user.dto.request.FormSignupRequestDto;
import com.backend.simya.domain.user.entity.LoginType;
import com.backend.simya.domain.user.entity.Role;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.repository.UserRepository;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponseStatus;
import com.backend.simya.global.util.S3Uploader;
import com.backend.simya.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

import static com.backend.simya.global.common.BaseResponseStatus.*;

/**
 * 회원가입, 유저정보조회 등의 API를 구현하기 위한 Service 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;


    @Transactional
    public void formSignup(FormSignupRequestDto formSignupRequestDto,
                           MultipartFile profileImage) throws BaseException {
        if (userRepository.existsByEmail(formSignupRequestDto.getEmail())) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        try {
            String profileImageUrl;
            if(profileImage == null){
                profileImageUrl = "default";
            } else {
                profileImageUrl = s3Uploader.uploadImage(profileImage);
            }
            User newUser = formSignupRequestDto.toEntity(passwordEncoder);
            Profile mainProfile = formSignupRequestDto.getProfile().toEntity(true, profileImageUrl);
            newUser.addProfile(mainProfile);
            userRepository.save(newUser);
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw new BaseException(POST_FAIL_USER);
        }
    }


    //== 유저, 권한정보를 가져오는 메소드 ==//
    // email 을 기준으로 정보를 가져온다.
    @Transactional(readOnly = true)
    public User getUserWithAuthorities(String username) throws BaseException {
        try {
            return userRepository.findOneWithAuthoritiesByEmail(username).orElseThrow(
                    () -> new BaseException(FAILED_TO_FIND_USER)
            );
        } catch (Exception exception) {
            if (userRepository.findByEmail(username).isPresent()) {
                throw new BaseException(ACCESS_ONLY_ADMIN);
            } else {
                throw new BaseException(GET_FAIL_USERINFO);
            }
        }
    }

    // SecurityContext에 저장된 email 에 해당하는 유저, 권한의 정보만 가저온다.
    @Transactional(readOnly = true)
    public User getMyUserWithAuthorities() throws BaseException {

        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByEmail).orElseThrow(
                () -> new BaseException(FAILED_TO_FIND_USER)
        );

    }

    @Transactional
    public void withdraw(Long userId) throws BaseException {
        try {
            userRepository.deleteById(userId);
        } catch (Exception exception) {
            throw new BaseException(DELETE_FAIL_USER);
        }
    }

    /**
     * 채팅 메시지 발송자 - 세션 유저에서 대표 프로필 닉네임으로 설정 시 사용
     */
    @Transactional
    public Profile getSessionToMainProfile(String sessionUserName) throws BaseException{
        User user = userRepository.findByEmail(sessionUserName).orElseThrow(
                () -> new BaseException(BaseResponseStatus.FAILED_TO_FIND_USER)
        );
        Profile profile = user.getProfileList().get(user.getMainProfile());

        log.info("[Header] simpUser: {} => 심야식당 서비스에서 찾은 유저: {} / 대표 프로필: {}", sessionUserName, user, profile);

        return profile;

    }

}
