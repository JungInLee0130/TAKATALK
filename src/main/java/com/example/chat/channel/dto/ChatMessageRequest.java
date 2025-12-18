package com.example.chat.channel.dto;

import lombok.Getter;

@Getter
public class ChatMessageRequest {
    private Long channelId;
    private String content;
}
