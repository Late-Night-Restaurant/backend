package com.backend.simya.domain.chat.dto;

import com.backend.simya.domain.profile.entity.Profile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("chatProfile")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomProfile  {

    private long profileId;
    private String nickname;
    private String comment;
    private String picture;

    public static ChatRoomProfile create(Profile profile) {
        ChatRoomProfile chatRoomProfile = new ChatRoomProfile();
        chatRoomProfile.profileId = profile.getProfileId();
        chatRoomProfile.nickname = profile.getNickname();
        chatRoomProfile.comment = profile.getComment();
        chatRoomProfile.picture = profile.getPicture();
        return chatRoomProfile;
    }
}
