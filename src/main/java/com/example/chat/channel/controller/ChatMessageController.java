package com.example.chat.channel.controller;

import com.example.chat.channel.dto.ChatMessageRequest;
import com.example.chat.channel.dto.ChatMessageResponse;
import com.example.chat.channel.dto.ChatMessageResponse2;
import com.example.chat.channel.service.ChatMessageService;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

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

        ChatMessageResponse2 response = chatMessageService.save(userDetails, request);
        messagingTemplate.convertAndSend("/sub/channel/" + request.getChannelId(), response);
    }
}
