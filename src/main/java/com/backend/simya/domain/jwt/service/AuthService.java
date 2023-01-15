package com.backend.simya.domain.jwt.service;

import com.backend.simya.domain.jwt.dto.request.TokenRequestDto;
import com.backend.simya.domain.jwt.dto.response.TokenDto;
import com.backend.simya.domain.jwt.entity.RefreshToken;
import com.backend.simya.domain.jwt.repository.RefreshTokenRepository;
import com.backend.simya.domain.user.dto.request.LoginDto;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.repository.UserRepository;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.config.jwt.JwtFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.backend.simya.global.common.BaseResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;


    // 롤백 문제 발생, @Transactional과 예외처리 관련
    // 왜 UsernameNotFound가 아닌 Exception이 올까?
    //@Transactional
    public TokenDto login(LoginDto loginDto) throws BaseException {
        try{
            Authentication authentication = authenticate(loginDto);  // 인증
            return authorize(authentication);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    // Authencation 객체를 만들어 인증한 뒤, Context에 저장
    @Transactional
    public Authentication authenticate(LoginDto loginDto) throws UsernameNotFoundException, BaseException {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        } catch (AuthenticationException e) {
            throw new BaseException(USERS_INVALID_ACCESS);
        } catch (Exception exception) {
            throw new BaseException(USERS_NOT_AUTHORIZED);
        }
    }

    // 토큰 발행으로 인가
    @Transactional
    public TokenDto authorize(Authentication authentication) throws BaseException {

        try {
            TokenDto tokenDto = tokenProvider.createToken(authentication.getName());

            RefreshToken refreshToken = RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();

            refreshTokenRepository.save(refreshToken);

            return tokenDto;
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_JWT);
        }
    }

    @Transactional
    public HttpHeaders inputTokenInHeader(TokenDto tokenDto) throws BaseException {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());
            return httpHeaders;
        } catch (Exception exception) {
            throw new BaseException(FAILED_JWT_IN_HEADER);
        }
    }

    // 로그인한 사용자 여부에 대한 검증 시 Header 에서 토큰 값을 가져온다.
    public String getJwt() throws BaseException {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            log.info("AuthService - getJwt() : header에 저장된 Access Token {}", request.getHeader("Authorization"));
            return request.getHeader("Authorization").substring(7);
        } catch (Exception exception) {
            throw new BaseException(INVALID_JWT);
        }
    }

    public String getUsername() throws BaseException {
        try {
            String accessToken = getJwt();
            if (accessToken == null) {
                throw new BaseException(EMPTY_JWT);
            }
            // TODO Access Token 만료 여부 체크 로직 추가

            Claims claims = tokenProvider.parseClaims(accessToken);
            log.info("AuthService - getUsername() : {}", claims);

            return claims.get("sub", String.class);
        } catch (Exception e) {
            throw new BaseException(INVALID_JWT);
        }
    }

    public User authenticateUser() throws BaseException {
        String username = getUsername();
        return userRepository.findByEmail(username).orElseThrow(
                () -> new BaseException(USERS_NOT_FOUND)
        );
    }

    public boolean validateClaim(String accessToken) throws BaseException {
        try {
            Claims claims = tokenProvider.parseClaims(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            // TODO 토큰 만료 시, TokenRequestDto (AccessToken, RefreshToken 넘겨서 갱신)

            return false;
        } /*catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new BaseException(MALFORMED_JWT);
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다.");
            throw new BaseException(UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new BaseException(INVALID_JWT);
        }*/

    }



    // TODO validateClaim -> reissue 에 넣어서 만료여부 체크 TokenProvider 거치지 않도록
    /**
     * 토큰 만료 시 재발급하는 메소드
     */
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            log.debug("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 유저 정보 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 유저 ID 를 기반으로 Refresh Token 값을 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            log.debug("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.createToken(authentication.getName());

        // 6. Repository 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateToken(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

}
