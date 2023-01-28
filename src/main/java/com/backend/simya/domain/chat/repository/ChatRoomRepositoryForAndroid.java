package com.backend.simya.domain.chat.repository;

import com.backend.simya.domain.chat.dto.ChatRoomForAndroid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ChatRoomRepositoryForAndroid {

    private static final String CHAT_ROOMS = "CHAT_ROOM";
    public static final String USER_COUNT = "USER_COUNT";
    public static final String ENTER_INFO = "ENTER_INFO";

    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, ChatRoomForAndroid> hashOpsChatRoom;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Long> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    /**
     * 특정 채팅방 조회
     */
    public ChatRoomForAndroid findRoomById(Long roomId) {
        return hashOpsChatRoom.get(CHAT_ROOMS, roomId);
    }

    /**
     * 채팅방 생성 : 서버 간 채팅방 공유를 위해 Redis hash 에 저장한다.
     */
    public void saveRoom(ChatRoomForAndroid chatRoom) {
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
    }

    /**
     * 유저가 입장한 채팅방 정보(roomId)와 유저 세션 정보(sessionId) 매핑하여 저장
     */
    public void saveUserEnterInfo(String sessionId, Long roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    /**
     * 유저 세션으로 입장한 상태인 채팅방 ID 조회 : setUserEnterInfo() 로 저장했던 것을 찾는 과정
     */
    public Long findUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    /**
     * 유저 세션 정보와 매핑된 채팅방 정보 (sessionId + roomId) 삭제
     */
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    public void removeChatRoom(Long roomId) {
        hashOpsChatRoom.delete(CHAT_ROOMS, roomId);
    }

    /**
     * 채팅방 유저 수 조회
     */
    public Long findUserCount(Long roomId) {
        return Long.parseLong(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    /**
     * 채팅방에 새로운 유저가 입장한 경우 => 인원 수 +1
     */
    public Long plusUserCount(Long roomId) {
        return Optional.ofNullable(
                valueOps.increment(USER_COUNT + "_" + roomId)
        ).orElse(0L);
    }

    /**
     * 채팅방에서 유저가 퇴장한 경우 => 인원 수 -1
     */
    public Long minusUserCount(Long roomId) {
        return Optional.ofNullable(
                valueOps.decrement(USER_COUNT + "_" + roomId)
        ).filter(count -> count > 0).orElse(0L);
    }
}
