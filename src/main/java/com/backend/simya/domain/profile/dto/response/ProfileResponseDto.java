package com.backend.simya.domain.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponseDto {

    private Long profileId;

    private String nickname;

    private String username;
}
