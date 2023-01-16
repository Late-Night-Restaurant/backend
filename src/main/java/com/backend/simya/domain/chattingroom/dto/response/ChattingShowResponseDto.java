package com.backend.simya.domain.chattingroom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ChattingShowResponseDto {
    private Long chattingId;
    private String chattingRoomName;
    private String name;
    private String picture;
    private String comment;
}
