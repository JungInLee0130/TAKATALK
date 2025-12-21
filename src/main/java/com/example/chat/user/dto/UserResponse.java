package com.example.chat.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {
    private String nickname;
    private String profileImageUrl;

    @Builder
    public UserResponse(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
