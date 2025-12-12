package com.example.chat.channel.dto;

import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.entity.Chats;
import com.example.chat.visitor.entity.Visitors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChatResponse {
    private Channels channel;
    private Chats chat;
    private List<Visitors> visitorsList = new ArrayList<>();

    public ChatResponse(Channels channel) {
        this.channel = channel;
    }

    public void setChannel(Channels channel) {
        this.channel = channel;
    }

    public void setChat(Chats chat) {
        this.chat = chat;
    }

    public void setVisitorsList(List<Visitors> visitorsList) {
        this.visitorsList = visitorsList;
    }
}
