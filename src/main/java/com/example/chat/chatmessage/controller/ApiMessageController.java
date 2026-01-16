package com.example.chat.chatmessage.controller;

import com.example.chat.chatmessage.dto.ChatMessageRequest;
import com.example.chat.chatmessage.dto.ChatMessageResponse;
import com.example.chat.chatmessage.service.ChatMessageService;
import com.example.chat.chatmessage.service.MessageService;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ApiMessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageResponse;

    @MessageMapping("/chatmessage/save")
    public void save(Principal principal,
                     @Payload ChatMessageRequest request){
        if (principal == null) {
            throw new CustomException(ErrorCode.SESSION_INVALID_ERROR); // 세션만료
        }

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails userDetails = (CustomUserDetails) authenticationToken.getPrincipal();

        ChatMessageResponse response = messageResponse.save(userDetails, request);
        messagingTemplate.convertAndSend("/sub/channel/" + request.getChannelId(), response);
    }
}
