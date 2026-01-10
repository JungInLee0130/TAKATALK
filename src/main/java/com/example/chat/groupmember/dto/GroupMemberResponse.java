package com.example.chat.groupmember.dto;

import com.example.chat.global.file.FileUtil;
import com.example.chat.groupmember.entity.GroupMember;
import com.example.chat.user.entity.SiteUser;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class GroupMemberResponse {
    private Long siteUserId;
    private String nickname;
    private String profile;

    @Builder
    private GroupMemberResponse(Long siteUserId, String nickname, String profile) {
        this.siteUserId = siteUserId;
        this.nickname = nickname;
        this.profile = FileUtil.getEffectiveProfile(profile);
    }

    public static GroupMemberResponse from(GroupMember groupMember) {
        SiteUser siteUser = groupMember.getSiteUser();  // 프록시로 가져오지말고 fetch join으로 가져오기

        return GroupMemberResponse.builder()
                .siteUserId(siteUser.getId())   // siteUser 칼럼 참조하므로
                .nickname(siteUser.getNickname())
                .profile(siteUser.getProfile())
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GroupMemberResponse that = (GroupMemberResponse) obj;
        return Objects.equals(siteUserId, that.siteUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(siteUserId);
    }

    @Override
    public String toString() {
        return "VisitorDto{" +
                "siteUserId=" + siteUserId +
                ", nickname='" + nickname + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
