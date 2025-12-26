package com.example.chat.channel.dto;

import com.example.chat.channel.domain.ChannelType;

public record CreateChannelRequest (
                                    Long groupId,
                                    Long categoryId,
                                    String channelName,
                                    ChannelType channelType,
                                    Boolean isSecret){
}
