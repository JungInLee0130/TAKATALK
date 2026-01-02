package com.example.chat.group.dto;

import com.example.chat.category.dto.CategoryResponse;
import com.example.chat.channel.dto.ChannelResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChannelGroupResponse {
    private List<CategoryResponse> categorizedChannels = new ArrayList<>();
    private List<ChannelResponse> uncategorizedChannels = new ArrayList<>();

    public ChannelGroupResponse(List<CategoryResponse> categorizedChannels, List<ChannelResponse> uncategorizedChannels) {
        this.categorizedChannels = categorizedChannels;
        this.uncategorizedChannels = uncategorizedChannels;
    }
}
