package com.example.chat.category.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CateGoryCreateForm {
    private String categoryName;
    private Boolean isSecret;

    public CateGoryCreateForm(String categoryName, Boolean isSecret) {
        this.categoryName = categoryName;
        this.isSecret = isSecret;
    }

    @Override
    public String toString() {
        return "CategoryCreateRequest{" +
                "name='" + categoryName + '\'' +
                ", isSecret=" + isSecret +
                '}';
    }
}
