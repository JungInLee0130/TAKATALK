package com.example.chat.groupmember.entity;

import com.example.chat.global.auditing.BaseTimeEntity;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.entity.Group;
import com.example.chat.groupmember.domain.GroupRole;
import com.example.chat.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "group_members")
public class GroupMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siteuser_id")
    private SiteUser siteUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    private GroupRole role;

    private Boolean isOnline = false;

    @Builder
    private GroupMember(SiteUser siteUser, Group group, GroupRole role) {
        this.siteUser = siteUser;
        this.group = group;
        this.role = role;
    }

    public static GroupMember create(SiteUser siteUser, Group group, GroupRole role) {
        return GroupMember.builder()
                .siteUser(siteUser)
                .group(group)
                .role(role)
                .build();
    }

    public void validateManagerRole(GroupRole role) {
        if (role != GroupRole.OWNER
                && role != GroupRole.ADMIN) {
            throw new CustomException(ErrorCode.GROUP_PERMISSION_DENIED);
        }
    }
}
