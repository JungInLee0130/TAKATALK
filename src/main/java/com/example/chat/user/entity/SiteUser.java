package com.example.chat.user.entity;

import com.example.chat.global.auditing.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siteuser_id")
    private Long id;

    private String nickname;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    private String profileImageUrl;

    @Builder
    public SiteUser(String nickname, String username, String password, String email
            , LocalDate birthday) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.role = RoleType.USER;
        this.profileImageUrl = "meeng.png";
    }

    public void updateNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }

    public void updatePassword(String password) {
        if (password != null) {
            this.password = password;
        }
    }
}
