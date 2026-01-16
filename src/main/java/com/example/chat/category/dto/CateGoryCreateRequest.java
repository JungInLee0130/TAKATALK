package com.example.chat.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CateGoryCreateRequest {
    @NotNull
    private String name;
    @NotNull
    private Boolean isSecret;

    public CateGoryCreateRequest(String name, Boolean isSecret) {
        this.name = name;
        this.isSecret = isSecret;
    }
}
