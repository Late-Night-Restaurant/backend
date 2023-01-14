package com.backend.simya.domain.user.dto.request;

import com.backend.simya.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Email(message = "이메일 형식을 확인해주세요.")
    @NotNull(message = "이메일을 입력해주세요.")
    @Size(min = 3, max = 50)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    public static UserDto from(User user) {
        if (user == null) return null;

        return UserDto.builder()
                .email(user.getEmail())
                .build();
    }

}
