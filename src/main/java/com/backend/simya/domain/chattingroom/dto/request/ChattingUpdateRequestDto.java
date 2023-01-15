package com.backend.simya.domain.chattingroom.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChattingUpdateRequestDto {
    private Long chattingRoomId;
    private Long userId;
    private String signboardImageUrl;
    private String chattingRoomName;
    private String comment;
}
