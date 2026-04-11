package com.example.chat.chatmessage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatMessageRequest {
    @NotNull
    private Long channelId;
    @NotNull
    private String content;
}
