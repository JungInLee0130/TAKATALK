package com.example.chat.user.entity;

import com.example.chat.global.auditing.BaseTimeEntity;
import com.example.chat.user.domain.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;

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

    private String profile;

    @Builder
    public SiteUser(String nickname, String username, String password, String email
            , LocalDate birthday, String profile) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.role = RoleType.USER;
        this.profile = profile;
    }

    public void updateNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    public void updateProfile(String profile) {
        this.profile = profile;
    }

    public void updatePassword(String password) {
        if (password != null) {
            this.password = password;
        }
    }
}
