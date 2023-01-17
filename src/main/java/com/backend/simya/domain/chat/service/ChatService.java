package com.backend.simya.domain.chat.service;

import com.backend.simya.domain.chat.dto.ChatRoom;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

import static com.backend.simya.global.common.BaseResponseStatus.FAILED_TO_SEND_MESSAGE;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct   // 의존성 주입이 완료된 후, 초기화하기 위해 실행되는 메서드 (빈의 초기화가 완료된 후에 수행됨이 보장 => 여러 번 초기화되는 것 방지)
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);   // 채팅방 리스트에 새로 개설한 채팅방 추가
        return chatRoom;
    }

    public <T> void sendMessage(WebSocketSession session, T message) throws BaseException {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(FAILED_TO_SEND_MESSAGE);
        }
    }
}
