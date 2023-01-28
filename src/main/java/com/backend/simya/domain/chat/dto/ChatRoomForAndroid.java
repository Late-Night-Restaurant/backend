package com.backend.simya.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomForAndroid implements ChatRoomInterface {

    private static final long serialVersionUID = 6494678977089006639L;

    private Long roomId;
    private Long userCount;

    public static ChatRoomForAndroid create(Long roomId) {
        return ChatRoomForAndroid.builder()
                .roomId(roomId)
                .userCount(0L)
                .build();
    }
}
