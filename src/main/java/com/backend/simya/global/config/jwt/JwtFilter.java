package com.backend.simya.global.config.jwt;

import com.backend.simya.domain.jwt.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * JWT를 위한 커스텀 필터 생성
 * OncePerRequestFilter 과 차이점 비교: https://velog.io/@chrkb1569/OncePerRequestFilter와-GenericFilterBean
 *
 * 모든 Request 요청은 이 필터를 거치므로 토큰 정보가 없거나 유효하지 않으면 정상적으로 수행되지 않는다.
 * 요청이 정상적으로 Controller 까지 도착했다면 SecurityContext 에 유저의 정보가 존재한다는 것이 보장된다.
 * 이때 DB 에 유저정보가 있는지 직접 조회한 것이 아니라 토큰에 실린 유저 정보를 조회한 것이므로, 탈퇴 등에 의해 DB에서 유저가 삭제된 경우는 Service 단에서 따로 고려를 해줘야 한다.
 *
 */
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Access-Token";
    public static final String REFRESH_HEADER = "Refresh-Token";
    public static final String BEARER_PREFIX = "Access ";

    private final TokenProvider tokenProvider;

    /**
     * 실제 필터링 로직 doFilter 메서드에 구현
     * doFilter : 토큰의 인증정보를 현재 쓰레드의 SecurityContext에 저장하는 역할 수행
     * 📍Authentication 객체 -> SecurityContext에 저장 -> ContextHolder
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);   // resolveToken() 을 통해 Request Header에서 토큰 받아오기 -> 유효성 검증
        String requestURI = httpServletRequest.getRequestURI();

        // 유효성 검증 로직
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {  // validateToken() 으로 토큰의 유효성 검사 후 정상 토큰이면 SecurityContext 에 저장
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // 필터링을 하려면 토큰 정보 필요 -> Request Header에서 토큰 정보를 꺼내오는 메소드
    private String resolveToken(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION_HEADER);
        log.info("request(Access-Token) = {}", request.getHeader("Access-Token"));
        log.info("request(Authorization) = {}", request.getHeader("Authorization"));
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            log.info("StringUtils.hasText 실행 - {}", accessToken.substring(7));
            return accessToken.substring(7);
        }
        return null;
    }

}

