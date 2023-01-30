package com.backend.simya.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatMessageCustom {
    public ChatMessageCustom() {

    }

    @Builder
    public ChatMessageCustom(ChatMessage.MessageType type, String roomId, String sender, String token, String message, long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.token = token;
        this.message = message;
        this.userCount = userCount;
    }

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private ChatMessage.MessageType type;   // 메시지 유형
    private String roomId;   // 방 번호
    private String sender;
    private String token;// 발신자
    private String message;  // 메시지 데이터
    private long userCount;  // 채팅방 인원 수: 채팅방 내에서 메시지가 전달될 때 인원 수 갱신
}