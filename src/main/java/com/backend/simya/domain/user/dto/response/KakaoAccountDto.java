package com.backend.simya.domain.user.dto.response;

import lombok.Data;

@Data
public class KakaoAccountDto {

    /*

    [Kakao] 동의 항목
    필수 - 닉네임 (profile_nickname)
    선택 - 카카오계정 이메일 (account_email)

    *카카오 Developers REST API 문서 참고
    https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#kakaoaccount

     */

    public Long id;   // 회원번호
    public String connected_at;  // 서비스에 연결된 시각(UTC)
    public Properties properties;  // 사용자 프로퍼티
    public KakaoAccount kakao_account;  // 카카오계정 정보

    @Data
    public class Properties {
        public String nickname;
        public String profile_image;
        public String thumbnail_image;
    }

    @Data
    public class KakaoAccount {  // 내부 클래스로 정의
        public Boolean profile_nickname_needs_agreement;
        public Boolean profile_image_needs_agreement;
        public KakaoProfile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;

        @Data
        public class KakaoProfile {
            public String nickname;
            public String profile_image_url;
            public String thumbnail_image_url;
            public Boolean is_default_image;
        }
    }
}
