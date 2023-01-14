package com.backend.simya.domain.profile.dto.request;

import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class ProfileRequestDto {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 1, max = 20)
    private String nickname;

    private String comment;

    private String picture;

    private User user;

    // TODO User 에서 addProfileList 할 때 한번에 처리하기
    public void setUserProfile(User user) {
        this.user = user;
    }

    public Profile toEntity() {
        return Profile.builder()
                .nickname(nickname)
                .comment(comment)
                .picture(picture)
                .user(user)
                .isRepresent(false)
                .activated(true)
                .build();
    }

}
