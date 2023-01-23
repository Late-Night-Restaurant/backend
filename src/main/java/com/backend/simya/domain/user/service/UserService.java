package com.backend.simya.domain.user.service;


import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.repository.ProfileRepository;
import com.backend.simya.domain.user.dto.request.UserDto;
import com.backend.simya.domain.user.entity.LoginType;
import com.backend.simya.domain.user.entity.Role;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.repository.UserRepository;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend.simya.global.common.BaseResponseStatus.*;

/**
 * 회원가입, 유저정보조회 등의 API를 구현하기 위한 Service 클래스
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto formSignup(UserDto userDto) throws BaseException {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        try {
            User newUser = User.builder()
                    .email(userDto.getEmail())
                    .pw(passwordEncoder.encode(userDto.getPassword()))
                    .loginType(LoginType.FORM)
                    .role(Role.ROLE_USER)
                    .activated(true)
                    .build();

            Profile mainProfile = Profile.builder()
                    .nickname(userDto.getProfile().getNickname())
                    .user(newUser)
                    .comment(userDto.getProfile().getComment())
                    .picture(userDto.getProfile().getPicture())
                    .isRepresent(true)
                    .build();
            newUser.addProfile(mainProfile);
            profileRepository.save(mainProfile);

            return UserDto.from(userRepository.save(newUser));

        } catch (Exception exception) {
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

}
