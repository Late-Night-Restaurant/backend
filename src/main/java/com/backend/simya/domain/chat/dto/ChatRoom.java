package com.backend.simya.domain.chat.dto;

import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
import com.backend.simya.domain.profile.entity.Profile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

/**
 * 채팅방 DTO
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;   // TODO 이야기 집(House)과 연결
    private long userCount;  // 채팅방 인원 수
    private List<ProfileResponseDto> profileList = new ArrayList<>();

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

    public void addProfile(Profile profile) {
        this.profileList.add(ProfileResponseDto.from(profile));
    }

    public void deleteProfile(Profile profile) {
        this.profileList.remove(ProfileResponseDto.from(profile));
    }
}

