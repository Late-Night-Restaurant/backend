/*
package com.backend.simya.domain.chat.controller;

import com.backend.simya.domain.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatPubController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/simya/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        messagingTemplate.convertAndSend("/sub/simya/chat/room/" + message.getRoomId(), message);
    }
}
*/
