package com.example.chat.category.dto;

import com.example.chat.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryGetResponse {
    private Long id;
    private String name;

    @Builder
    private CategoryGetResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryGetResponse from(Category category) {
        return CategoryGetResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
