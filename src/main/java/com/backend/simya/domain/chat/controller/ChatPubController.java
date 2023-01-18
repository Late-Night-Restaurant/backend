package com.backend.simya.domain.chat.controller;

import com.backend.simya.domain.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatPubController {

    private final SimpMessageSendingOperations messagingTemplate;  // 특정 Broker 로 메시지 전달

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
        if (ChatMessageDto.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);  // roomId 로 채팅방을 구분하여 메세지를 송신
    }
}
