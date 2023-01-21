package com.backend.simya.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 채팅 메시지 타입 : 채팅방 입장(ENTER), 대화하기(TALK), 퇴장(QUIT)
 * - ENTER : 처음 채팅방에 들어오는 상태
 * - TALK : 이미 세션에 연결되어 있고 채팅중인 상태
 * - QUIT : 채팅방을 나가는 상태
 *
 * => 인원 수 표시 가능, 입장/퇴장 알림 서버에서 처리 가능
 */
@Getter @Setter
public class ChatMessage {

    public ChatMessage() {

    }

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
    }

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private MessageType type;   // 메시지 유형
    private String roomId;   // 방 번호
    private String sender;   // 발신자
    private String message;  // 메시지 데이터
    private long userCount;  // 채팅방 인원 수: 채팅방 내에서 메시지가 전달될 때 인원 수 갱신
}

