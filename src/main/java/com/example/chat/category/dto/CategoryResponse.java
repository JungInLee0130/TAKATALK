package com.example.chat.category.dto;

import com.example.chat.category.entity.Categories;
import com.example.chat.channel.dto.ChannelResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CategoryResponse {
    private Long id;
    private String name;

    private List<ChannelResponse> channels = new ArrayList<>();

    @Builder
    public CategoryResponse(Long id, String name, List<ChannelResponse> channels) {
        this.id = id;
        this.name = name;
        this.channels = channels;
    }
}
