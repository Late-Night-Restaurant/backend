package com.backend.simya.domain.jwt.controller;

import com.backend.simya.domain.jwt.dto.response.TokenDto;
import com.backend.simya.domain.jwt.service.TokenProvider;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final TokenProvider tokenProvider;

    @PostMapping("/reissue")
    public BaseResponse<TokenDto> reissue() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            String accessToken = request.getHeader("Access-Token").substring(7);
            String refreshToken = request.getHeader("Refresh-Token").substring(8);

            return new BaseResponse<>(tokenProvider.reissue(accessToken));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
