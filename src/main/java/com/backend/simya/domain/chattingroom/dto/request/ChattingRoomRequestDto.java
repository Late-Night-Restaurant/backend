package com.backend.simya.domain.chattingroom.dto.request;

import com.backend.simya.domain.chattingroom.entity.Category;
import com.backend.simya.domain.chattingroom.entity.ChattingRoom;
import com.backend.simya.domain.profile.entity.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChattingRoomRequestDto {

    private Long profileId;  // 방장 프로필_id
    private Category category;
    private String signboardImageUrl;
    private String chattingRoomName;
    private String comment;

    public ChattingRoom toEntity(Profile profile) {
        return ChattingRoom.builder()
                .profile(profile)
                .category(category)
                .chattingRoomName(chattingRoomName)
                .comment(comment)
                .capacity(0)
                .signboardImageUrl(signboardImageUrl)
                .open(false)
                .activated(true)
                .build();
    }

}
