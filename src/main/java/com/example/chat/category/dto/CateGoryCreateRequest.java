package com.example.chat.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class CateGoryCreateRequest {
    private Long groupId;
    private String categoryName;
    private Boolean isSecret;

    @Builder
    private CateGoryCreateRequest(Long groupId, String categoryName, Boolean isSecret) {
        this.groupId = groupId;
        this.categoryName = categoryName;
        this.isSecret = isSecret;
    }

    public static CateGoryCreateRequest create(Long groupId, String categoryName, Boolean isSecret) {
        return CateGoryCreateRequest.builder()
                .groupId(groupId)
                .categoryName(categoryName)
                .isSecret(isSecret)
                .build();
    }
}
