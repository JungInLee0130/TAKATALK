package com.example.chat.channel.dto;

import com.example.chat.channel.domain.ChannelType;
import com.example.chat.channel.entity.Channel;
import lombok.Builder;

@Builder
public record ChannelResponse(Long id,
                              String name,
                              ChannelType type,
                              Boolean isSecret) {

    public static ChannelResponse from (Channel channel){
        return ChannelResponse.builder()
                .id(channel.getId())
                .name(channel.getName())
                .type(channel.getType())
                .isSecret(channel.getIsSecret())
                .build();
    }
}


