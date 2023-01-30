package com.backend.simya.domain.user.dto.response;

import com.backend.simya.domain.house.entity.Topic;
import com.backend.simya.domain.profile.entity.Profile;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormLoginResponseDto {

    private Long profileId;
    private String nickname;
    private String picture;
    private String comment;
    private String accessToken;
    private String refreshToken;

    public static FormLoginResponseDto from(Profile profile, String accessToken, String refreshToken) {
        return FormLoginResponseDto.builder()
                .profileId(profile.getProfileId())
                .nickname(profile.getNickname())
                .comment(profile.getComment())
                .comment(profile.getComment())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
