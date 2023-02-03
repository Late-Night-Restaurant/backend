/*
package com.backend.simya.domain.chat.dto;

import com.backend.simya.domain.profile.entity.Profile;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ChatRoomProfileTest {

    private RedisTemplate<String, ChatRoomProfile> redisTemplate;

    @Test
    public void testRedisSet() {
        SetOperations<String, ChatRoomProfile> profileSet = redisTemplate.opsForSet();
        String key = "roomId";

        Profile profile = Profile.builder()
                .profileId(1L)
                .nickname("nickname")
                .comment("comment")
                .picture("picture")
                .build();

        profileSet.add(key, ChatRoomProfile.create(profile));
        Set<ChatRoomProfile> testResult = profileSet.members(key);
        System.out.println(testResult);
    }

}*/
