package com.backend.simya.domain.chat.dto;

import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageForAndroid {

    public ChatMessageForAndroid() {

    }

    @Builder
    public ChatMessageForAndroid(MessageType type, Long roomId, ProfileResponseDto senderProfile, String message, long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.senderProfile = senderProfile;
        this.message = message;
        this.userCount = userCount;
    }

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private MessageType type;   // 메시지 유형
    private Long roomId;   // 방 번호
    private ProfileResponseDto senderProfile;   // 발신자
    private String message;  // 메시지 데이터
    private long userCount;  // 채팅방 인원 수: 채팅방 내에서 메시지가 전달될 때 인원 수 갱신
}
