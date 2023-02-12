package com.backend.simya.domain.profile.dto.request;

import com.backend.simya.domain.profile.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 1, max = 20)
    private String nickname;

    private String comment;

    public Profile toEntity(boolean isMain, String pictureUrl) {
        return Profile.builder()
                .nickname(nickname)
                .comment(comment)
                .pictureUrl(pictureUrl)
                .isRepresent(isMain)
                .build();
    }

}
