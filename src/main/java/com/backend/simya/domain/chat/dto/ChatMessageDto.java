package com.backend.simya.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 채팅 메시지 타입 : 채팅방 입장(ENTER), 대화하기(TALK)
 * - ENTER : 처음 채팅방에 들어오는 상태
 * - TALK : 이미 세션에 연결되어 있고 채팅중인 상태
 */
@Getter @Setter
public class ChatMessageDto {

    public enum MessageType {
        ENTER, TALK
    }

    private MessageType type;   // 메시지 유형
    private String roomId;   // 방 번호
    private String sender;   // 발신자
    private String message;  // 메시지 데이터
}
