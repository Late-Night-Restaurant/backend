package com.backend.simya.domain.profile.dto.response;

import com.backend.simya.domain.user.dto.request.UserDto;
import com.backend.simya.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponseDto {

    private Long profileId;

    private String nickname;

    private User user;
}
