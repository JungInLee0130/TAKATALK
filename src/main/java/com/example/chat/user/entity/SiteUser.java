package com.example.chat.user.entity;

import com.example.chat.user.domain.UserCreateForm;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class SiteUser {
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
            , LocalDate birthday) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.role = RoleType.USER;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
