package com.backend.simya.domain.chat.dto;

import com.backend.simya.domain.chat.service.ChatService;
import com.backend.simya.global.common.BaseException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

/**
 * 채팅방 DTO
 */
@Slf4j
@Getter
public class ChatRoom {

    private String roomId;
    private String name;   // 이야기 집(House)과 연결
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handlerActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender()+"님이 입장했습니다.");
        }
        sendMessage(chatMessage, chatService);
    }

    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> {
                    try {
                        chatService.sendMessage(session, message);
                    } catch (BaseException e) {
                        log.info("메시지 전송에 실패했습니다.");
                    }
                });
    }
}
