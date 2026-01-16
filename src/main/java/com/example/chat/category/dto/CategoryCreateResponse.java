package com.example.chat.category.dto;

import com.example.chat.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryCreateResponse {
    private Long groupId;
    private String categoryName;
    private Boolean isSecret;

    @Builder
    private CategoryCreateResponse(Long groupId, String categoryName, Boolean isSecret) {
        this.groupId = groupId;
        this.categoryName = categoryName;
        this.isSecret = isSecret;
    }

    public static CategoryCreateResponse from(Category category) {
        return CategoryCreateResponse.builder()
                .groupId(category.getGroup().getId())   // Lazy loading
                .categoryName(category.getName())
                .isSecret(category.getIsSecret())
                .build();
    }
}
