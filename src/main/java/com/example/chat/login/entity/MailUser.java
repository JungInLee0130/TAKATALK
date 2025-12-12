package com.example.chat.login.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Builder
    public MailUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
