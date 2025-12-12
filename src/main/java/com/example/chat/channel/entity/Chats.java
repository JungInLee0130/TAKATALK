package com.example.chat.channel.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    private String content;

    @OneToOne
    @JoinColumn(name = "channel_id")
    private Channels channel;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    //private MediaType mediaType;
}
