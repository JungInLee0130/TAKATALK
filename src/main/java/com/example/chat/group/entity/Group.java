package com.example.chat.group.entity;

import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "groups")
@SQLDelete(sql = "UPDATE groups SET is_deleted = true where group_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String name;

    private String profile;

    @Column(unique = true)
    private String inviteCode;  // 서버별 고유 초대코드

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Builder
    private Group(String name, String inviteCode) {
        this.name = name;
        this.inviteCode = inviteCode;
    }

    public static Group create(String name) {
        String inviteCode = UUID.randomUUID().toString().substring(0, 8);

        return Group.builder()
                .name(name)
                .inviteCode(inviteCode)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateProfile(String profile) {
        this.profile = profile == null ? null : profile;
    }

    public void validateInviteCode(String inviteCode) {
        // TODO : inviteCode 유효기간 부여 -> Date 검증
    }

    // 초대 코드 업데이트
    public void updateInviteCode(){
        this.inviteCode = UUID.randomUUID().toString().substring(0, 8);
    }
}
