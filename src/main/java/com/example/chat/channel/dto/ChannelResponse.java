package com.example.chat.channel.dto;

import com.example.chat.channel.domain.ChannelType;
import lombok.Builder;

@Builder
public record ChannelResponse(Long id,
                              String name,
                              ChannelType type,
                              Boolean isSecret) {


}


