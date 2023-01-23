package com.backend.simya.domain.chat.service;

import com.backend.simya.domain.chat.dto.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * Redis 구독 서비스
 *
 * Redis에 메시지 발행이 될 때까지 대기했다가 메시지 발행되면 해당 메시지를 읽어 처리하는 리스너이다.
 * 여기서 Redis에 메시지가 발행되면 해당 메시지가 ChatMessage로 변환되고 messaging Template를 이용하여
 * 채팅방의 모든 WebSocket 클라이언트들에게 메시지를 전달할 수 있다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis 에서 pub 으로 메시지가 발행되면 대기하고 있던 onMessage 가 해당 메시지를 받아서 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객체로 매핑
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            // WebSocket 구독자에게 채팅 메시지 Send
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}
