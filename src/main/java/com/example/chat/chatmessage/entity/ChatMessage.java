package com.example.chat.chatmessage.entity;

import com.example.chat.channel.entity.Channel;
import com.example.chat.chatmessage.domain.ChatMessageType;
import com.example.chat.global.auditing.BaseTimeEntity;
import com.example.chat.user.entity.SiteUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatmessage_id")
    private Long id;

    @Size(max = 2000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siteuser_id")
    private SiteUser siteUser;

    private Boolean isModified = false;

    @Enumerated(EnumType.STRING)
    private ChatMessageType type;

    @Builder
    private ChatMessage(String content,
                       Channel channel,
                       SiteUser siteUser,
                       ChatMessageType type) {
        this.content = content;
        this.channel = channel;
        this.siteUser = siteUser;
        this.type = type;
    }

    public static ChatMessage create (String content,
                                      Channel channel,
                                      SiteUser siteUser,
                                      ChatMessageType type) {
        return ChatMessage.builder()
                .content(content)
                .channel(channel)
                .siteUser(siteUser)
                .type(type != null ? type : ChatMessageType.TALK)
                .build();
    }

    public void updateContent(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            this.isModified = true;     // 내용을 수정할때 수정됨으로 표시하는게 좋음.
        }
    }
}
