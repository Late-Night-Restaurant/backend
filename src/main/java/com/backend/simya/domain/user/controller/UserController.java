package com.backend.simya.domain.user.controller;

import com.backend.simya.domain.jwt.dto.response.TokenDto;
import com.backend.simya.domain.jwt.service.AuthService;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.user.dto.request.FormSignupRequestDto;
import com.backend.simya.domain.user.dto.request.LoginRequestDto;
import com.backend.simya.domain.user.dto.response.FormLoginResponseDto;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import com.backend.simya.global.common.ValidErrorDetails;
import com.backend.simya.global.config.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.Optional;

import static com.backend.simya.global.common.BaseResponseStatus.*;

@Slf4j
@RestController
@RequestMapping("/simya")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/form-signup")
    public BaseResponse formSignup(@Valid @RequestPart FormSignupRequestDto formSignupRequestDto,
                                        @RequestPart(value = "image", required = false) MultipartFile profileImage,
                                        Errors errors) {
        if (errors.hasErrors()) {
            log.info("ValidError: {}", errors);
            ValidErrorDetails errorDetails = new ValidErrorDetails();
            return new BaseResponse<>(REQUEST_ERROR, errorDetails.validateHandling(errors));
        }
        try {
            userService.formSignup(formSignupRequestDto, profileImage);
            return new BaseResponse<>(SUCCESS_TO_SIGNUP);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PostMapping("/form-login")
    public BaseResponse formLogin(@Valid @RequestBody LoginRequestDto loginDto, HttpServletResponse response, Errors errors) {

        if (errors.hasErrors()) {
            log.info("ValidError: {}", errors);
            ValidErrorDetails errorDetails = new ValidErrorDetails();
            return new BaseResponse<>(REQUEST_ERROR, errorDetails.validateHandling(errors));
        }

        try{
            TokenDto tokenDto = authService.login(loginDto);
            String accessToken = tokenDto.getAccessToken();
            String refreshToken = tokenDto.getRefreshToken();

            // 헤더에 토큰 추가
            response.addHeader(JwtFilter.AUTHORIZATION_HEADER, "Access " + accessToken);
            response.addHeader(JwtFilter.REFRESH_HEADER, "Refresh " + refreshToken);

            User currentUser = userService.getMyUserWithAuthorities();
            Profile mainProfile = currentUser.getProfileList().get(currentUser.getMainProfile());

            return new BaseResponse<>(FormLoginResponseDto.from(mainProfile, accessToken, refreshToken));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/mypage")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // USER, ADMIN 권한 모두 허용
    public BaseResponse<User> getMyUserInfo() {
        try {
            User userResponse = userService.getMyUserWithAuthorities();
            return new BaseResponse<>(userResponse);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")   // ADMIN 권한만 허용 -> API를 호출 가능한 권한을 제한함
    public BaseResponse<User> getUserInfo(@PathVariable String username) {
        try {
            User userResponse = userService.getUserWithAuthorities(username);
            return new BaseResponse<>(userResponse);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    @DeleteMapping("/withdraw")
    public BaseResponse withdraw() {
        try {
            User loginUser = userService.getMyUserWithAuthorities();
            userService.withdraw(loginUser.getUserId());
            return new BaseResponse(SUCCESS_TO_WITHDRAW);
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

    @GetMapping("/logout")
    public BaseResponse logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            return new BaseResponse(SUCCESS_TO_LOGOUT);
        } catch (Exception e) {
            return new BaseResponse(FAILED_TO_LOGOUT);
        }
    }
}
