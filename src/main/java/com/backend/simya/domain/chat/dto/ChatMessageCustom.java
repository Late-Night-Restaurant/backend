package com.backend.simya.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatMessageCustom {
    public ChatMessageCustom() {

    }

    @Builder
    public ChatMessageCustom(ChatMessage.MessageType type, String roomId, Long profileId, String sender, String pictureUrl, String token, String message, long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.profileId = profileId;
        this.sender = sender;
        this.pictureUrl = pictureUrl;
        this.token = token;
        this.message = message;
        this.userCount = userCount;
    }

    public enum MessageType {
        ENTER, TALK, QUIT, FREEZE, BAN, FORCE
    }

    private ChatMessage.MessageType type;   // 메시지 유형
    private String roomId;   // 방 번호
    private String sender;
    private Long profileId;
<<<<<<< HEAD
    private String pictureUrl;
    private String token;// 발신자
=======
    private String picture;
    private String token;    // 발신자
>>>>>>> 92d9477 ([ feat ] ChatMessage ENUM 추가)
    private String message;  // 메시지 데이터
    private long userCount;  // 채팅방 인원 수: 채팅방 내에서 메시지가 전달될 때 인원 수 갱신
}
