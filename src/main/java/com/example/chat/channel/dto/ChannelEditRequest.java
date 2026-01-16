package com.example.chat.channel.dto;

import com.example.chat.channel.domain.ChannelType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChannelEditRequest {
    @NotNull
    @Size(min = 3, max = 25, message = "채널 명은 3 ~ 25자로 해주세요.")
    private String name;
    @NotNull
    private ChannelType type;
    @NotNull
    private Boolean isSecret;

    public ChannelEditRequest(String name, ChannelType type, Boolean isSecret) {
        this.name = name;
        this.type = type;
        this.isSecret = isSecret;
    }
}
