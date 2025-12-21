package com.example.chat.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {
    private String nickname;
    private String profile;

    @Builder
    public UserResponse(String nickname, String profile) {
        this.nickname = nickname;
        this.profile = profile;
    }
}
