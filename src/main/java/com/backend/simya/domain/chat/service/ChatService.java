package com.backend.simya.domain.chat.service;


import com.backend.simya.domain.chat.dto.ChatMessage;
import com.backend.simya.domain.chat.dto.ChatMessageCustom;
import com.backend.simya.domain.chat.dto.ChatRoom;
import com.backend.simya.domain.chat.repository.ChatRoomRepository;
import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
import com.backend.simya.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;


/**
 * 채팅 메시지 발송을 일원화하기 위한 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    /**
     * 채팅방 조회
     * - destination 정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIdx = destination.lastIndexOf('/');
        if (lastIdx != -1) {
            return destination.substring(lastIdx + 1);
        } else {
            return "";
        }
    }

    public void openChatRoom(HouseOpenRequestDto houseOpenRequestDto) {
        chatRoomRepository.saveChatRoom(ChatRoom.createForAndroid(String.valueOf(houseOpenRequestDto.getHouseId())));
    }

    public void enterChatRoom(String sessionId, String roomId) {
        chatRoomRepository.setUserEnterInfo(sessionId, roomId);
        chatRoomRepository.plusUserCount(roomId);  // 인원 수 +1
    }

    public void exitChatRoom(String sessionId, String roomId) {
        chatRoomRepository.removeUserEnterInfo(sessionId);
        chatRoomRepository.minusUserCount(roomId);  // 인원 수 +1
    }

    public void closeChatRoom(Long roomId) {
        chatRoomRepository.removeChatRoom(String.valueOf(roomId));
    }


    /*
     * 메시지 발송 - 지정한 웹 소켓 세션으로 메시지 발송
     * TALK 상태일 때 실행
     */

    public void sendChatMessage(ChatMessage message) {
        message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));

        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            log.info(message.getSender() + "님이 방에 입장했습니다");
            message.setMessage(message.getSender() + "님이 방에 입장했습니다");
            message.setSender("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(message.getType())) {
            log.info(message.getSender() + "님이 방에서 나갔습니다.");
            message.setMessage(message.getSender() + "님이 방에서 나갔습니다.");
            message.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }

    public void sendChatMessage(ChatMessageCustom message) {
        message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));

        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            log.info(message.getSender() + "님이 방에 입장했습니다");
            message.setMessage(message.getSender() + "님이 방에 입장했습니다");
            message.setSender("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(message.getType())) {
            log.info(message.getSender() + "님이 방에서 나갔습니다.");
            message.setMessage(message.getSender() + "님이 방에서 나갔습니다.");
            message.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }


}

