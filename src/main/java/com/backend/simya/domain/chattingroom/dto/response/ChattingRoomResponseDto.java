package com.backend.simya.domain.chattingroom.dto.response;

import com.backend.simya.domain.chattingroom.entity.Category;
import com.backend.simya.domain.profile.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChattingRoomResponseDto {

    private Long chattingRoomId;
    private Long profileId;
    private Category category;
    private String signboardImageUrl;
    private String chattingRoomName;
    private String comment;
    private boolean open;

}
