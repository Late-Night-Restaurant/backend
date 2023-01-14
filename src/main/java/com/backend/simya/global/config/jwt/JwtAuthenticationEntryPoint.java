package com.backend.simya.global.config.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 유저정보의 부재 등 유효한 자격증명을 제공하지 않고 접근 시, 401 Unauthorized 에러를 리턴하는 클래스
 * => 정상적인 JWT 가 오지 않은 경우에 대해 필터링
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);   // 401 Unauthorized
    }
}
