package com.example.chat.visitor.dto;

import com.example.chat.global.file.FileUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class VisitorDto {
    private Long siteUserId;
    private String nickname;
    private String profile;

    @Builder
    public VisitorDto(Long siteUserId, String nickname, String profile) {
        this.siteUserId = siteUserId;
        this.nickname = nickname;
        this.profile = FileUtil.getEffectiveProfile(profile);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VisitorDto that = (VisitorDto) obj;
        return Objects.equals(siteUserId, that.siteUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(siteUserId);
    }
}
