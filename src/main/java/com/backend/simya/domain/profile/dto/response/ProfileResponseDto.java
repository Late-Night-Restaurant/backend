package com.backend.simya.domain.profile.dto.response;

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
public class ProfileResponseDto {

    private Long profileId;
    private String nickname;
    private String comment;
    private String picture;

    public static ProfileResponseDto from(Profile profile) {
        return ProfileResponseDto.builder()
                .profileId(profile.getProfileId())
                .nickname(profile.getNickname())
                .comment(profile.getComment())
                .picture(profile.getPicture())
                .build();
    }
}
