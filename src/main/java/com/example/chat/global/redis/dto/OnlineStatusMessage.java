package com.example.chat.global.redis.dto;

public record OnlineStatusMessage(
        Long userId,
        String nickname,
        Status status
) {
    public enum Status {
        ONLINE, OFFLINE
    }
}
