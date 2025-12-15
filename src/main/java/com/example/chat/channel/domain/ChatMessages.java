package com.example.chat.channel.domain;

import com.example.chat.channel.entity.Channels;
import com.example.chat.user.entity.SiteUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatmessage_id")
    private Long id;

    @Size(max = 2000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channels channel;

    @OneToOne
    @JoinColumn(name = "siteuser_id")
    private SiteUser siteUser;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Builder
    public ChatMessages(String content, Channels channel, SiteUser siteUser) {
        this.content = content;
        this.channel = channel;
        this.siteUser = siteUser;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = LocalDateTime.now();
    }
}
