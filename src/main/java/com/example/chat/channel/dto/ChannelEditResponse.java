package com.example.chat.channel.dto;

import com.example.chat.channel.domain.ChannelType;
import com.example.chat.channel.entity.Channel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelEditResponse {
    private Long groupId;
    private Long categoryId;
    private Long channelId;
    private String name;
    private ChannelType type;
    private Boolean isSecret;

    @Builder
    private ChannelEditResponse(Long groupId, Long categoryId, Long channelId, String name, ChannelType type, Boolean isSecret) {
        this.groupId = groupId;
        this.categoryId = categoryId;
        this.channelId = channelId;
        this.name = name;
        this.type = type;
        this.isSecret = isSecret;
    }

    public static ChannelEditResponse from(Channel channel) {
        return ChannelEditResponse.builder()
                .groupId(channel.getGroup().getId())
                .categoryId(channel.getCategory().getId())
                .channelId(channel.getId())
                .name(channel.getName())
                .type(channel.getType())
                .isSecret(channel.getIsSecret())
                .build();
    }
}
