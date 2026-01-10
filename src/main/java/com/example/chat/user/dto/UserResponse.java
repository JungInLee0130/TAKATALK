package com.example.chat.user.dto;

import com.example.chat.global.file.FileUtil;
import com.example.chat.user.entity.SiteUser;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {
    private String nickname;
    private String profile;

    @Builder
    private UserResponse(String nickname, String profile) {
        this.nickname = nickname;
        this.profile = FileUtil.getEffectiveProfile(profile);
    }

    public static UserResponse from(SiteUser siteUser) {
        return UserResponse.builder()
                .nickname(siteUser.getNickname())
                .profile(siteUser.getProfile())
                .build();
    }
}
