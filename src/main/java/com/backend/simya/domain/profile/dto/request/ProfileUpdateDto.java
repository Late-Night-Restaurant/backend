package com.backend.simya.domain.profile.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileUpdateDto {

    private String nickname;
    
    private String comment;

    private boolean isRepresent;
}
