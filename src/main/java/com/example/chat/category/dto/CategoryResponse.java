package com.example.chat.category.dto;

import com.example.chat.category.entity.Category;
import com.example.chat.channel.dto.ChannelResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CategoryResponse {
    private Long id;
    private String name;
    private List<ChannelResponse> channels;

    @Builder
    private CategoryResponse(Long id, String name, List<ChannelResponse> channels) {
        this.id = id;
        this.name = name;
        this.channels = (channels == null) ? new ArrayList<>() : channels;
    }

    // Entity -> DTO
    public static CategoryResponse from (Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .channels(category.getChannels().stream() // channel : lazy loading
                        .map(ChannelResponse::from)
                        .toList())
                .build();
    }
}
