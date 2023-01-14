package com.backend.simya.domain.jwt.service;

import com.backend.simya.domain.jwt.dto.response.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.backend.simya.domain.user.entity.Role.ROLE_USER;

/**
 * JWT 토큰에 관련된 암호화, 복호화, 검증 로직이 이루어지는 클래스
 */
@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;   // Access Token 만료 기한: 1일
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // Refresh Token 만료 기한: 7일
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey) {
        // JWT 토큰 생성 시 사용될 암호화 키 값 생성자에서 지정
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(String email) {

        long now = (new Date()).getTime();   // 토큰의 만료 시간 설정
        Date accessTokenExpireTime = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpireTime = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = publishAccessToken(email, accessTokenExpireTime);
        String refreshToken = publishRefreshToken(refreshTokenExpireTime);

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpireTime.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public String publishAccessToken(String email, Date expireTime) {
        return Jwts.builder()
                .setSubject(email)    // payload "sub" : "name"
                .claim(AUTHORITIES_KEY, ROLE_USER)     // payload "auth" : "ROLE_USER"
                .signWith(key, SignatureAlgorithm.HS512) // header "alg" : HS512 (해싱 알고리즘)
                .setExpiration(expireTime)    // payload "exp" (10자리)
                .compact();
    }

    // 껍데기 리프레쉬 토큰
    public String publishRefreshToken(Date expireTime) {
        return Jwts.builder()
                .setExpiration(expireTime)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }



    // Token 에 담겨 있는 정보를 이용해 Authentication 객체를 리턴하는 메소드
    public Authentication getAuthentication(String accessToken) {   // Access Token 에만 유저 정보를 담기 때문에 명시적으로 파라미터에 Access Token 을 넘겨줌

        Claims claims = parseClaims(accessToken);    // JWT 토큰을 복호화하여 토큰에 들어 있는 정보를 꺼냄

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // Claim 으로 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 유저 객체를 만들어 최종적으로 Authentication 객체를 리턴
        User principal = new User(claims.getSubject(), "", authorities);  // UserDetails 객체를 생성 -> UsernamePasswordAuthenticationToken 형태로 리턴

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);   // SecurityContext 를 사용하기 위한 절차 (SecurityContext 가 Authentication 객체를 저장하기 때문)
    }

    // 토큰의 유효성 검증을 수행하는 메소드
    public boolean validateToken(String token) {
        try {  // 토큰 파싱 후 발생하는 Exception을 종류에 따라 Catch!
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);   // Jwts 모듈이 알아서 Exception 을 던져줌
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }

        return false;
    }

    // 토큰을 파라미터로 받아 클레임 생성 (토큰의 만료 여부와 상관없이 정보를 꺼낼 수 있음)
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
