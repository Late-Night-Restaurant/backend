package com.backend.simya.domain.jwt.controller;

import com.backend.simya.domain.jwt.dto.request.TokenRequestDto;
import com.backend.simya.domain.jwt.dto.response.TokenDto;
import com.backend.simya.domain.jwt.service.AuthService;
import com.backend.simya.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public BaseResponse<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return new BaseResponse<>(authService.reissue(tokenRequestDto));
    }

}
