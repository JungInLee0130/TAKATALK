package com.example.chat.global.redis;

import com.example.chat.global.redis.dto.OnlineStatusMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // 1. Redis에서 받은 메시지를 역직렬화 (record 타입으로)
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            OnlineStatusMessage statusMessage = objectMapper.readValue(publishMessage, OnlineStatusMessage.class);

            log.info("Redis Subscribed Message : User {} is {}", statusMessage.userId(), statusMessage.status());

            // 2. 해당 메시지를 웹소켓 구독자들에게 전송
            // 이 서버 인스턴스에 연결된 클라이언트들이 정보를 받게 됨.
            messagingTemplate.convertAndSend("/sub/global/online-event", statusMessage);
            
            // 전체 명단도 갱신하여 보낼 수 있음 (상태에 따라 Redis Set 등 활용)
        } catch (IOException e) {
            log.error("Redis Subscriber Error: ", e);
        }
    }
}
