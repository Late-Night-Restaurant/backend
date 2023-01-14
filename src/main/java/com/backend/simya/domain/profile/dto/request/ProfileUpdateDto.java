package com.backend.simya.domain.profile.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileUpdateDto {

    private Long profileId;

    private String nickname;
    
    private String comment;

    private String picture;

    private boolean isRepresent;
}
