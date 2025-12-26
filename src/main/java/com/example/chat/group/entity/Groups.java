package com.example.chat.group.entity;

import com.example.chat.user.entity.SiteUser;
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
public class Groups {
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
    public Groups(String name) {
        this.name = name;
        generateInviteCode();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateProfile(String profile) {
        if (profile != null) {
            this.profile = profile;
        }
    }

    // 서버 생성시 자동으로 초대코드 생성
    public void generateInviteCode(){
        this.inviteCode = UUID.randomUUID().toString().substring(0, 8);
    }
}
