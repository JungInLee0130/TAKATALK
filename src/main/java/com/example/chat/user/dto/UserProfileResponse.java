package com.example.chat.user.dto;

import com.example.chat.global.file.FileUtil;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponse {
    private String nickname;
    private String profile;

    @Builder
    public UserProfileResponse(String nickname, String profile) {
        this.nickname = nickname;
        this.profile = FileUtil.getEffectiveProfile(profile);
    }
}
