package com.backend.simya.domain.user.dto.request;

import com.backend.simya.domain.profile.dto.request.ProfileRequestDto;
import com.backend.simya.domain.user.entity.LoginType;
import com.backend.simya.domain.user.entity.Role;
import com.backend.simya.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormSignupRequestDto {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Size(min = 3, max = 50)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    private ProfileRequestDto profile;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.getEmail())
                .pw(passwordEncoder.encode(this.getPassword()))
                .loginType(LoginType.FORM)
                .role(Role.ROLE_USER)
                .activated(true)
                .build();
    }
}
