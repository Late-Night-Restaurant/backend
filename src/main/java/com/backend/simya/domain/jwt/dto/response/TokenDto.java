package com.backend.simya.domain.jwt.dto.response;

import lombok.*;

/**
 * Token 정보를 Response 할 때 사용할 TokenDto 객체
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

}