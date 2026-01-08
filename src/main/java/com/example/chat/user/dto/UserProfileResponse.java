package com.example.chat.user.dto;

import com.example.chat.global.file.FileUtil;
import com.example.chat.user.entity.SiteUser;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponse {
    private String nickname;
    private String username;
    private String profile;

    @Builder
    private UserProfileResponse(String nickname, String username, String profile) {
        this.nickname = nickname;
        this.username = username;
        this.profile = FileUtil.getEffectiveProfile(profile);
    }

    public static UserProfileResponse from(SiteUser siteUser) {
        return UserProfileResponse.builder()
                .nickname(siteUser.getNickname())
                .username(siteUser.getUsername())
                .profile(siteUser.getProfile())
                .build();
    }
}
