package com.example.chat.channel.dto;

import com.example.chat.channel.domain.ChatMessageType;
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
    public ChatMessageResponse(Long channelId, String profile, String nickname, LocalDateTime createdAt, String content, Boolean isModified,
                               Long chatMessageId, ChatMessageType type) {
        this.channelId = channelId;
        this.profile = profile;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.content = content;
        this.isModified = isModified;
        this.chatMessageId = chatMessageId;
        this.type = type;
    }
}
