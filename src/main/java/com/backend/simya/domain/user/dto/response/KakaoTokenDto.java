package com.backend.simya.domain.user.dto.response;

import lombok.Data;

/**
 * 카카오에서 보내는 Access Token 을 매핑하는 클래스
 * TODO 인가코드 -> Http Request 완성 후 필요한 파라미터의 필드명 가져오기
 * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token-response
 */
@Data
public class KakaoTokenDto {

    private String token_type;
    private String access_token;
    private String refresh_token;
    private String id_token;
    private int expires_in;
    private int refresh_token_expires_in;
    private String scope;
}
