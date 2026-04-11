package com.example.chat.chatmessage.controller;

import com.example.chat.chatmessage.dto.ChatMessageRequest;
import com.example.chat.chatmessage.dto.ChatMessageResponse;
import com.example.chat.chatmessage.service.StompMessageService;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.user.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class StompMessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final StompMessageService stompMessageService;

    @MessageMapping("/chatmessage/save")
    public void save(Principal principal,
                     @Valid @Payload ChatMessageRequest request){
        if (principal == null) {
            throw new CustomException(ErrorCode.SESSION_INVALID_ERROR); // 세션만료
        }

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails userDetails = (CustomUserDetails) authenticationToken.getPrincipal();
        ChatMessageResponse response = stompMessageService.save(userDetails, request);  // 저장
        messagingTemplate.convertAndSend("/sub/channel/" + request.getChannelId(), response);   // send
    }
}
