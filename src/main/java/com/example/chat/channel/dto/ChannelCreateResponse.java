package com.example.chat.channel.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelCreateResponse {
    private Long channelId;
    private Long groupId;

    @Builder

    public ChannelCreateResponse(Long channelId, Long groupId) {
        this.channelId = channelId;
        this.groupId = groupId;
    }
}
