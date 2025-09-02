package com.example.chat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDto {
    private String roomId;
    private String sender;
    private String message;
    private MessageType messageType;

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    @Builder
    public ChatMessageDto(String roomId, String sender, String message, MessageType messageType) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.messageType = messageType;
    }

    public void updateMessage(String message) {
        this.message = message;
    }
}
