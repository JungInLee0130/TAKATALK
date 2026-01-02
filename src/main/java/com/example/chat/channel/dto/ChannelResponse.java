package com.example.chat.channel.dto;

import com.example.chat.channel.domain.ChannelType;
import com.example.chat.channel.entity.Channels;
import lombok.Builder;

@Builder
public record ChannelResponse(Long id,
                              String name,
                              ChannelType type,
                              Boolean isSecret) {

    public static ChannelResponse from (Channels channel){
        return ChannelResponse.builder()
                .id(channel.getId())
                .name(channel.getName())
                .type(channel.getType())
                .isSecret(channel.getIsSecret())
                .build();
    }
}


