package com.example.chat.chatmessage.dto;

import com.example.chat.chatmessage.domain.ChatMessageType;
import com.example.chat.chatmessage.entity.ChatMessage;
import com.example.chat.global.file.FileUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponse {
    private Long channelId;     // 채널 아이디

    private Long chatMessageId;  // 채팅 메시지 아이디 (무한스크롤 구현시 필요)
    private String profile;     // 프로필
    private String nickname;    // 닉네임
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;    // 생성 날짜
    private String content;     // 내용
    private Boolean isModified; // 수정 여부
    private ChatMessageType type;    // 메시지타입

    @Builder
    private ChatMessageResponse(Long channelId, String profile, String nickname, LocalDateTime createdAt, String content, Boolean isModified,
                               Long chatMessageId, ChatMessageType type) {
        this.channelId = channelId;
        this.profile = FileUtil.getEffectiveProfile(profile);
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.content = content;
        this.isModified = isModified;
        this.chatMessageId = chatMessageId;
        this.type = type;
    }

    /*public static ChatMessageResponse create(Long channelId, String profile, String nickname, LocalDateTime createdAt, String content, Boolean isModified,
                                             Long chatMessageId, ChatMessageType type) {
        return ChatMessageResponse.builder()
                .type(type)
                .channelId(channelId)
                .profile(profile)
                .nickname(nickname)
                .content(content)
                .createdAt(createdAt)
                .isModified(isModified)
                .chatMessageId(chatMessageId)
                .build();
    }*/

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .channelId(chatMessage.getChannel().getId())    // channel
                .profile(chatMessage.getSiteUser().getProfile())    // siteUser
                .nickname(chatMessage.getSiteUser().getNickname())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .isModified(chatMessage.getIsModified())
                .chatMessageId(chatMessage.getId())
                .type(chatMessage.getType())
                .build();
    }
}
