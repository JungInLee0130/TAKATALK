package com.example.chat.chatmessage.controller;

import com.example.chat.chatmessage.dto.ChatMessageResponse;
import com.example.chat.chatmessage.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ChatMessage Api", description = "채팅메시지 관련 Api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/channel/{channelId}/chatMessage")
public class ApiChatMessageController {
    private final ChatMessageService chatMessageService;

    @Operation(summary = "채팅 내역 조회", description = "특정 채널의 과거 메시지를 무한 스크롤(slice)로 가져옵니다.")
    @GetMapping("/history")
    @ResponseBody
    public List<ChatMessageResponse> getChatHistory(
            @PathVariable(name = "channelId") Long channelId,
            @RequestParam(required = false) Long lastMessageId) {
        return chatMessageService.getOldMessage(channelId, lastMessageId);
    }
}
