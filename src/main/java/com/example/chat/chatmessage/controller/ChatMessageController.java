package com.example.chat.chatmessage.controller;

import com.example.chat.chatmessage.dto.ChatMessageRequest;
import com.example.chat.chatmessage.dto.ChatMessageResponse;
import com.example.chat.chatmessage.service.ChatMessageService;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chatmessage/save")
    public void save(Principal principal,
                     @Payload ChatMessageRequest request){
        if (principal == null) {
            throw new CustomException(ErrorCode.SESSION_INVALID_ERROR); // 세션만료
        }

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails userDetails = (CustomUserDetails) authenticationToken.getPrincipal();

        ChatMessageResponse response = chatMessageService.save(userDetails, request);
        messagingTemplate.convertAndSend("/sub/channel/" + request.getChannelId(), response);
    }

    @GetMapping("/chatmessage/history/{channelId}")
    @ResponseBody
    public List<ChatMessageResponse> getChatHistory(
            @PathVariable(name = "channelId") Long channelId,
            @RequestParam(required = false) Long lastMessageId) {
        return chatMessageService.getOldMessage(channelId, lastMessageId);
    }
}
