package com.example.chat.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CategoryUpdateRequest {
    @NotNull
    private String name;
    @NotNull
    private Boolean isSecret;

    public CategoryUpdateRequest(String name, Boolean isSecret) {
        this.name = name;
        this.isSecret = isSecret;
    }
}
