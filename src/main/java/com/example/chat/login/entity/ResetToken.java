package com.example.chat.login.entity;

import com.example.chat.global.auditing.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResetToken extends BaseTimeEntity {

    public static final long DEFAULT_EXPIRATION_MINUTES = 15L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @Column(unique = true)
    private String uuid;
    private LocalDateTime expiresAt;    // 만료시간은 비즈니스 로직이므로 유지

    @Builder
    private ResetToken(String email, String uuid, LocalDateTime expiresAt) {
        this.email = email;
        this.uuid = uuid;
        this.expiresAt = expiresAt;
    }

    // [방법 1] 기본 만료 시간 사용
    public static ResetToken create(String email){
        return create(email, DEFAULT_EXPIRATION_MINUTES);
    }

    public static ResetToken create(String email, Long expirationMinutes) {
        long minutes = expirationMinutes != null ? expirationMinutes : DEFAULT_EXPIRATION_MINUTES;

        return ResetToken.builder()
                .email(email)
                .uuid(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusMinutes(minutes))
                .build();
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
