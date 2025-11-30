package com.example.chat.chatting.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    private String roomId;
    private String sender;
    private String message;
    private MessageType type;


    public enum MessageType {
        ENTER, TALK, QUIT
    }

    public ChatMessage(String roomId, String message) {
        this.message = message;
    }

    public void updateSender(String sender) {
        this.sender = sender;
    }

    public void updateMessage(String message) {
        this.message = message;
    }
}
