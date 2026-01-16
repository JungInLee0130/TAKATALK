package com.example.chat.group.dto;

import com.example.chat.category.dto.CategoryResponse;
import com.example.chat.channel.dto.ChannelResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GroupChannelResponse {
    private List<CategoryResponse> categorizedChannels = new ArrayList<>();
    private List<ChannelResponse> uncategorizedChannels = new ArrayList<>();

    @Builder
    private GroupChannelResponse(List<CategoryResponse> categorizedChannels, List<ChannelResponse> uncategorizedChannels) {
        this.categorizedChannels = categorizedChannels;
        this.uncategorizedChannels = uncategorizedChannels;
    }

    public static GroupChannelResponse of(List<CategoryResponse> categorizedChannels, List<ChannelResponse> uncategorizedChannels) {
        return GroupChannelResponse.builder()
                .categorizedChannels(categorizedChannels)
                .uncategorizedChannels(uncategorizedChannels)
                .build();
    }
}
