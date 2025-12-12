package com.example.chat.channel.dto;

import com.example.chat.channel.domain.ChannelType;

public record ChannelResponse(Long channelId,
                              String channelName,
                              ChannelType channelType,
                              Boolean channelIsSecret) {

}


