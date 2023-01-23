package com.backend.simya.domain.user.controller;

import com.backend.simya.domain.jwt.dto.response.TokenDto;
import com.backend.simya.domain.jwt.service.AuthService;
import com.backend.simya.domain.user.dto.request.LoginDto;
import com.backend.simya.domain.user.dto.request.UserDto;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import com.backend.simya.global.common.BaseResponseStatus;
import com.backend.simya.global.common.ValidErrorDetails;
import com.backend.simya.global.config.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.backend.simya.global.common.BaseResponseStatus.REQUEST_ERROR;
import static com.backend.simya.global.common.BaseResponseStatus.SUCCESS_TO_WITHDRAW;

@Slf4j
@RestController
@RequestMapping("/simya")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/form-signup")
    public BaseResponse signup(@Valid @RequestBody UserDto userDto, Errors errors) {

        if (errors.hasErrors()) {
            log.info("ValidError: {}", errors);
            ValidErrorDetails errorDetails = new ValidErrorDetails();
            return new BaseResponse<>(REQUEST_ERROR, errorDetails.validateHandling(errors));
        }

        try {
            return new BaseResponse<>(userService.formSignup(userDto));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/form-login")
    public BaseResponse formLogin(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response, Errors errors) {

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

            return new BaseResponse<>(tokenDto);
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
}
