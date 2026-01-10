package com.example.chat.channel.dto;

import com.example.chat.channel.entity.Channel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelCreateResponse {
    private Long channelId;
    private Long groupId;

    @Builder
    private ChannelCreateResponse(Long channelId, Long groupId) {
        this.channelId = channelId;
        this.groupId = groupId;
    }

    public static ChannelCreateResponse from(Channel channel) {
        return ChannelCreateResponse.builder()
                .channelId(channel.getId())
                .groupId(channel.getGroup().getId())
                .build();
    }
}
