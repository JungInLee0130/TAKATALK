package com.example.chat.chatmessage.service;

import com.example.chat.channel.entity.Channel;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.chatmessage.domain.ChatMessageType;
import com.example.chat.chatmessage.dto.ChatMessageRequest;
import com.example.chat.chatmessage.dto.ChatMessageResponse;
import com.example.chat.chatmessage.entity.ChatMessage;
import com.example.chat.chatmessage.repository.ChatMessageRepository;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChannelService channelService;
    private final UserService userService;
    
    public ChatMessageResponse save(CustomUserDetails userDetails, ChatMessageRequest request) {
        Channel channel = channelService.findById(request.getChannelId()); // 중간에 누군가가 채널을 삭제할수있기때문에 직접 불러와야함.
        SiteUser siteUser = userService.findById(userDetails.getId());

        ChatMessage chatMessage = ChatMessage.create(request.getContent(), channel, siteUser, ChatMessageType.TALK);
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        return ChatMessageResponse.from(savedMessage);
    }
}
