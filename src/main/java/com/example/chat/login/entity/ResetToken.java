package com.example.chat.login.entity;

import com.example.chat.global.auditing.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResetToken extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @Column(unique = true)
    private String uuid;
    private LocalDateTime expiresAt;    // 만료시간은 비즈니스 로직이므로 유지

    @Builder
    public ResetToken(String email, String uuid, Long expirationMinutes) {
        this.email = email;
        this.uuid = uuid;
        this.expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes != null ? expirationMinutes : 30);
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
