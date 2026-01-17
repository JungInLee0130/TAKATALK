package com.example.chat.chatmessage.controller;

import com.example.chat.chatmessage.dto.ChatMessageResponse;
import com.example.chat.chatmessage.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/chatmessage/history/{channelId}")
    @ResponseBody
    public List<ChatMessageResponse> getChatHistory(
            @PathVariable(name = "channelId") Long channelId,
            @RequestParam(required = false) Long lastMessageId) {
        return chatMessageService.getOldMessage(channelId, lastMessageId);
    }
}
