package com.backend.simya.domain.jwt.service;

import com.backend.simya.domain.jwt.dto.response.TokenDto;
import com.backend.simya.domain.jwt.entity.RefreshToken;
import com.backend.simya.domain.jwt.repository.RefreshTokenRepository;
import com.backend.simya.domain.user.dto.request.LoginRequestDto;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.repository.UserRepository;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.config.jwt.JwtFilter;
import io.jsonwebtoken.Claims;
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

import java.util.Optional;

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
    public TokenDto login(LoginRequestDto loginDto) throws BaseException {

        Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
        if (user.isEmpty()) {
            throw new BaseException(USERS_NOT_FOUND);
        } else {
            if (!user.get().isActivated()) {
                throw new BaseException(BANNED_USER_IN_LOGIN);
            }
        }


        try{
            Authentication authentication = authenticate(loginDto);  // 인증
            return authorize(authentication);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    // Authencation 객체를 만들어 인증한 뒤, Context에 저장
    @Transactional
    public Authentication authenticate(LoginRequestDto loginDto) throws UsernameNotFoundException, BaseException {
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
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Access " + tokenDto.getAccessToken());
            httpHeaders.add(JwtFilter.REFRESH_HEADER, "Refresh " + tokenDto.getRefreshToken());
            return httpHeaders;
        } catch (Exception exception) {
            throw new BaseException(FAILED_JWT_IN_HEADER);
        }
    }



    public String getUsername() throws BaseException {
        try {
            String accessToken = tokenProvider.getJwt().getAccessToken();
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

}
