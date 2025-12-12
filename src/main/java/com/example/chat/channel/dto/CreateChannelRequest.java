package com.example.chat.channel.dto;

import com.example.chat.channel.domain.ChannelType;

public record CreateChannelRequest (String channelName,
                                    ChannelType channelType,
                                    Boolean isSecret){
}
