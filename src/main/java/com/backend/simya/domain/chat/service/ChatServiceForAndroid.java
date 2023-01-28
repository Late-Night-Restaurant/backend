package com.backend.simya.domain.chat.service;

import com.backend.simya.domain.chat.dto.ChatMessageForAndroid;
import com.backend.simya.domain.chat.dto.ChatRoomForAndroid;
import com.backend.simya.domain.chat.repository.ChatRoomRepositoryForAndroid;
import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
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
public class ChatServiceForAndroid {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatRoomRepositoryForAndroid chatRoomRepository;

    /**
     * 채팅방 조회
     * - destination 정보에서 roomId 추출
     */
    public Long getRoomId(String destination) {
        int lastIdx = destination.lastIndexOf('/');
        if (lastIdx != -1) {
            return Long.parseLong(destination.substring(lastIdx + 1));
        } else {
            return null;
        }
    }

    public void openChatRoom(HouseOpenRequestDto houseOpenRequestDto) {
        chatRoomRepository.saveRoom(ChatRoomForAndroid.create(houseOpenRequestDto.getHouseId()));
    }

    public void enterChatRoom(String sessionId, Long roomId) {
        chatRoomRepository.saveUserEnterInfo(sessionId, roomId);
        chatRoomRepository.plusUserCount(roomId);  // 인원 수 +1
    }

    public void exitChatRoom(String sessionId, Long roomId) {
        chatRoomRepository.removeUserEnterInfo(sessionId);
        chatRoomRepository.minusUserCount(roomId);  // 인원 수 +1
    }

    public void closeChatRoom(Long roomId) {
        chatRoomRepository.removeChatRoom(roomId);
    }



    /*
     * 메시지 발송 - 지정한 웹 소켓 세션으로 메시지 발송
     * TALK 상태일 때 실행
     */
    public void sendChatMessage(ChatMessageForAndroid message) {
        message.setUserCount(chatRoomRepository.findUserCount(message.getRoomId()));

        if (ChatMessageForAndroid.MessageType.ENTER.equals(message.getType())) {
            log.info(message.getSenderProfile().getNickname() + "님이 방에 입장했습니다");
            message.setMessage(message.getSenderProfile().getNickname() + "님이 방에 입장했습니다");
        } else if (ChatMessageForAndroid.MessageType.QUIT.equals(message.getType())) {
            log.info(message.getSenderProfile().getNickname() + "님이 방에서 나갔습니다.");
            message.setMessage(message.getSenderProfile().getNickname() + "님이 방에서 나갔습니다.");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }

}


