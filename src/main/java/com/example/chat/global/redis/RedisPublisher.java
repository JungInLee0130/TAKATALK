package com.example.chat.global.redis;

import com.example.chat.global.redis.dto.OnlineStatusMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    public void publish(OnlineStatusMessage message) {
        // Redis 채널로 메시지 전송
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
