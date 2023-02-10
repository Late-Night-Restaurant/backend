package com.backend.simya.domain.chat.repository;

import com.backend.simya.domain.chat.dto.ChatRoom;
import com.backend.simya.domain.chat.dto.ChatRoomProfile;
import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.repository.ProfileRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 채팅방에 관련된 데이터를 처리하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
    private final ProfileRepository profileRepository;

    // Redis CacheKeys
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    public static final String USER_COUNT = "USER_COUNT";
    public static final String ENTER_INFO = "ENTER_INFO";
    public static final String PROFILE_LIST = "PROFILE_LIST";


    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;
    @Resource(name = "redisTemplate")
    private SetOperations<String, String> hashProfileList;

    /**
     * 모든 채팅방 조회
     */
    public List<ChatRoom> findAllRoom() {
        return hashOpsChatRoom.values(CHAT_ROOMS);
    }

    /**
     * 특정 채팅방 조회
     */
    public ChatRoom findRoomById(String id) {
        ChatRoom chatRoom = hashOpsChatRoom.get(CHAT_ROOMS, id);
//        List<ChatRoomProfile> profileList = getRoomProfileList(id);
//        log.info("ProfileList 드디어 받았다!! {} ", profileList);
//        if (!profileList.isEmpty()) chatRoom.setProfileList(profileList);
        return chatRoom;
    }

    /**
     * 채팅방 생성 : 서버 간 채팅방 공유를 위해 Redis hash 에 저장한다.
     */
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    /**
     * 채팅방 얼리기(삭제) : Redis에 저장되어 있던 채팅방 목록에서 삭제한다.
     */
    public ChatRoom freezeChatRoom(String id) {
        ChatRoom chatRoom = hashOpsChatRoom.get(CHAT_ROOMS, id);
        chatRoom.setFreeze(true);
        hashOpsChatRoom.delete(CHAT_ROOMS, id);   // TODO 그냥 freeze: true인 채로 저장할까
        return chatRoom;
    }

    public void saveChatRoom(ChatRoom chatRoom) {
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
    }

    /**
     * 유저가 입장한 채팅방 정보(roomId)와 유저 세션 정보(sessionId) 매핑하여 저장
     */
    public void setUserEnterInfo(String sessionId, String roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    /**
     * 유저 세션으로 입장한 상태인 채팅방 ID 조회 : setUserEnterInfo() 로 저장했던 것을 찾는 과정
     */
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    /**
     * 유저 세션 정보와 매핑된 채팅방 정보 (sessionId + roomId) 삭제
     */
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    /**
     * 채팅방 유저 수 조회
     */
    public long getUserCount(String roomId) {
        return Long.parseLong(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    public void removeChatRoom(String roomId) {
        hashOpsChatRoom.delete(CHAT_ROOMS, roomId);
    }


    /**
     * 채팅방에 새로운 유저가 입장한 경우 => 인원 수 +1
     */
    public long plusUserCount(String roomId) {
        return Optional.ofNullable(
                valueOps.increment(USER_COUNT + "_" + roomId)
        ).orElse(0L);
    }

    /**
     * 채팅방에서 유저가 퇴장한 경우 => 인원 수 -1
     */
    public long minusUserCount(String roomId) {
        return Optional.ofNullable(
                valueOps.decrement(USER_COUNT + "_" + roomId)
        ).filter(count -> count > 0).orElse(0L);
    }

    /**
     * 프로필 리스트에 추가
     */
    public void addRoomProfileList(Profile profile, String roomId) {
        Objects.requireNonNull(hashOpsChatRoom.get(CHAT_ROOMS, roomId)).addProfile(profile);   // roomRedis의 리스트에 추가
        log.info("프로필 리스트(Redis) 추가 전");
        hashProfileList.add(roomId, String.valueOf(profile.getProfileId()));
        log.info("프로필 리스트(Redis) 추가 후: {}", profile.getNickname());
        try {
            List<ChatRoomProfile> list = getRoomProfileList(roomId);
            log.info("addRoom - members() 성공: {}", list );
            log.info("isMemeber : {}", hashProfileList.isMember(roomId, String.valueOf(profile.getProfileId())));
            StringBuilder sb = new StringBuilder();
            for (ChatRoomProfile profiles : list ) {
                sb.append(" ").append(profiles.getNickname());
            }
            log.info("Profile List: " + sb.toString());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 프로필 리스트에서 제거
     */
    public void deleteRoomProfileList(Profile profile, String roomId) {
        Objects.requireNonNull(hashOpsChatRoom.get(CHAT_ROOMS, roomId)).deleteProfile(profile);  // roomRedis의 리스트에서 삭제
        hashProfileList.remove(roomId, String.valueOf(profile.getProfileId()));
        try {
            List<ChatRoomProfile> list  = getRoomProfileList(roomId);
            log.info("deleteRoom - members() 성공: {}", list );
            log.info("isMemeber : {}", hashProfileList.isMember(roomId, String.valueOf(profile.getProfileId())));
            StringBuilder sb = new StringBuilder();
            for (ChatRoomProfile profiles : list) {
                sb.append(" ").append(profiles.getNickname());
            }
            log.info("Profile List: " + sb.toString());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * roomId에 대한 프로필 리스트 가져오기
     */
    public List<ChatRoomProfile> getRoomProfileList(String roomId) {
        log.info("ChatRoomRepository-getRoomProfileList() 실행");
        log.info("hashProfileList.toString(): " + hashProfileList.toString());

        List<ChatRoomProfile> chatRoomProfileList = new ArrayList<>();
        try {
            List<String> profileIdSet = new ArrayList<>(hashProfileList.members(roomId));
            log.info("members() -> {}", profileIdSet);
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<profileIdSet.size(); i++) {
                log.info("profileId->Dto: {}", profileIdToDto(profileIdSet.get(i)));
                chatRoomProfileList.add(profileIdToDto(profileIdSet.get(i)));
                sb.append("\n").append(profileIdToDto(profileIdSet.get(i)).getNickname());
            }
            log.info("Profile List: " + sb.toString());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
        return chatRoomProfileList;
    }

    private ChatRoomProfile profileIdToDto(String profileId) {
        return ChatRoomProfile.create(profileRepository.findById(Long.parseLong(profileId)).orElseThrow(null));
    }

}
