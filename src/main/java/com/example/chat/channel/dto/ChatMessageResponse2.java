package com.example.chat.channel.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponse2 {
    private Long channelId;     // 채널 아이디
    private String profile;     // 프로필
    private String nickname;    // 닉네임
    private LocalDateTime createdAt;    // 생성 날짜
    private String content;     // 내용
    private Boolean isModified; // 수정 여부

    @Builder
    public ChatMessageResponse2(Long channelId, String profile, String nickname, LocalDateTime createdAt, String content, Boolean isModified) {
        this.channelId = channelId;
        this.profile = profile;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.content = content;
        this.isModified = isModified;
    }
}
