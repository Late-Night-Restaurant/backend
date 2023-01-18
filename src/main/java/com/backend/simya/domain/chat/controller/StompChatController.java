package com.backend.simya.domain.chat.controller;

import com.backend.simya.domain.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class StompChatController {

    private final SimpMessageSendingOperations messagingTemplate;  // 특정 Broker 로 메시지 전달

    /**
     * CLIENT 가 SEND 할 수 있는 경로
     * -> stompConfig 에서 설정한 applicationDestinationPrefixes + @MessageMapping 경로가 병합됨 => "/pub/chat/enter"
     */
    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto message) {
        message.setMessage(message.getSender() + "님이 채팅방에 참여했습니다.");
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);  // roomId 로 채팅방을 구분하여 메세지를 송신
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
