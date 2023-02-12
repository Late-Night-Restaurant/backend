package com.backend.simya.domain.user.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 로그인에 필요한 정보를 주고받는 DTO 객체
 */
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다,")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(min = 3, max = 50)
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 3, max = 100)
    private String password;
}
