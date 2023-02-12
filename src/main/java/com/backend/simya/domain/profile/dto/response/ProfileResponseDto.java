package com.backend.simya.domain.profile.dto.response;

import com.backend.simya.domain.profile.entity.Profile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProfileResponseDto {

    @JsonProperty("profileId")
    private Long profileId;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("pictureUrl")
    private String pictureUrl;

    public static ProfileResponseDto from(Profile profile) {
        return ProfileResponseDto.builder()
                .profileId(profile.getProfileId())
                .nickname(profile.getNickname())
                .comment(profile.getComment())
                .pictureUrl(profile.getPictureUrl())
                .build();
    }
}
