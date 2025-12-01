package com.example.chat.login.entity;

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
public class ResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @Column(unique = true)
    private String UUID;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @Builder
    public ResetToken(String email, String UUID, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.email = email;
        this.UUID = UUID;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
