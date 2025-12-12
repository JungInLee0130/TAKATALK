package com.example.chat.friends;

import lombok.Builder;
import lombok.NoArgsConstructor;


public class FriendsResponse {
    private String nickname;
    private String profile;

    @Builder
    public FriendsResponse(String nickname) {
        this.nickname = nickname;
    }
}
