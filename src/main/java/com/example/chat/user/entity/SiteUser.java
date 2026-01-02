package com.example.chat.user.entity;

import com.example.chat.global.auditing.BaseTimeEntity;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.user.domain.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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


    // 생성자 : 대입만. 외부 호출 차단
    @Builder
    private SiteUser(String nickname, String username, String password, String email
            , LocalDate birthday, RoleType role, String profile) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.role = role;
        this.profile = profile;
    }

    // 정적 팩토리 메서드 : 비즈니스 규칙 강제
    public static SiteUser create(String username, String nickname, String email,
                                  String rawPassword, LocalDate birthDay, String profile,
                                  PasswordEncoder passwordEncoder) {
        // 먼저 validation 가능
        return SiteUser.builder()
                .username(username)
                .nickname(nickname)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .birthday(birthDay)
                .role(RoleType.USER)
                .profile(profile)
                .build();
    }

    public void updateNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    public void updateProfile(String profile) {
        this.profile = profile;
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_EMAIL_OR_PASSWORD);
        }
        this.password = passwordEncoder.encode(rawPassword);
    }

    public void isPasswordMatched(PasswordEncoder passwordEncoder, String inputPassword) {
        if (!passwordEncoder.matches(inputPassword, this.password)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_EMAIL_OR_PASSWORD);
        }
    }
}
