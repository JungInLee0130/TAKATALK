package com.example.chat.category.dto;

import com.example.chat.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryEditResponse {
    private Long groupId;
    private Long categoryId;
    private String name;
    private Boolean isSecret;

    @Builder
    private CategoryEditResponse(Long groupId, Long categoryId, String name, Boolean isSecret) {
        this.groupId = groupId;
        this.categoryId = categoryId;
        this.name = name;
        this.isSecret = isSecret;
    }

    public static CategoryEditResponse from(Category category) {
        return CategoryEditResponse.builder()
                .groupId(category.getGroup().getId())
                .categoryId(category.getId())
                .name(category.getName())
                .isSecret(category.getIsSecret())
                .build();
    }
}
