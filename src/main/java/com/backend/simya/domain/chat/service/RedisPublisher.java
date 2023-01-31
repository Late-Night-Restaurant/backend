package com.backend.simya.domain.chat.service;

import com.backend.simya.domain.chat.dto.ChatMessage;
import com.backend.simya.domain.chat.dto.ChatMessageCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * Redis 발행 서비스
 *
 * Redis는 공통 주제(Topic)에 대하여 구독자(subscriber)에게 메시지를 발행(publish)할 수 있는 기능을 제공하는 서비스이다.
 * 이 서비스를 통해 메시지를 발행하면 대기 중이던 redis 구독 서비스가 메시지를 처리한다.
 */
@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessageCustom message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
