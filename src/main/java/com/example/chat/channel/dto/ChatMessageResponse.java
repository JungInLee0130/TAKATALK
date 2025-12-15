package com.example.chat.channel.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponse {
    private String profile;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public ChatMessageResponse(String profile, String nickname, String content, LocalDateTime createdAt) {
        this.profile = profile;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
    }
}
