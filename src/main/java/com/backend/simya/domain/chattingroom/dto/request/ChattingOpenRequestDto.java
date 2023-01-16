package com.backend.simya.domain.chattingroom.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChattingOpenRequestDto {

    private Long chattingRoomId;
    private Long userId;
    private int capacity;
    private String title;
    private String content;

}
