package com.backend.simya.domain.jwt.dto.response;

import com.backend.simya.domain.user.entity.User;
import lombok.Data;

@Data
public class RefreshResponseDto {

    String accessToken;
    User user;
}
