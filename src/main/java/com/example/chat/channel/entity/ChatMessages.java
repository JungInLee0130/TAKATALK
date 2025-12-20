package com.example.chat.channel.entity;

import com.example.chat.channel.domain.ChatMessageType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siteuser_id")
    private SiteUser siteUser;

    private LocalDateTime createdAt;

    private Boolean isModified;

    @Enumerated(EnumType.STRING)
    private ChatMessageType type;

    @Builder
    public ChatMessages(String content,
                        Channels channel,
                        SiteUser siteUser,
                        ChatMessageType type) {
        this.content = content;
        this.channel = channel;
        this.siteUser = siteUser;
        this.type = (type != null ? type : ChatMessageType.TALK);

        this.createdAt = LocalDateTime.now();
        this.isModified = false;    // 클라이언트가 조작가능하므로 처음 저장시 false로 고정
    }

    public void setContent(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            this.isModified = true;     // 내용을 수정할때 수정됨으로 표시하는게 좋음.
        }
    }
}
