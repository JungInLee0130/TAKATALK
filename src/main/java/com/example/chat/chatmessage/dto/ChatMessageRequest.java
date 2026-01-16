package com.example.chat.chatmessage.dto;

import lombok.Getter;

@Getter
public class ChatMessageRequest {
    private Long channelId;
    private String content;
}
