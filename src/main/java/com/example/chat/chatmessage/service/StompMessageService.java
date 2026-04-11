package com.example.chat.chatmessage.service;

import com.example.chat.channel.entity.Channel;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.chatmessage.domain.ChatMessageType;
import com.example.chat.chatmessage.dto.ChatMessageRequest;
import com.example.chat.chatmessage.dto.ChatMessageResponse;
import com.example.chat.chatmessage.entity.ChatMessage;
import com.example.chat.chatmessage.repository.ChatMessageRepository;
import com.example.chat.user.dto.UserResponse;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StompMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessageResponse save(CustomUserDetails userDetails, ChatMessageRequest request) {
        Channel channel = channelRepository.getReferenceById(request.getChannelId());
        SiteUser siteUser = userRepository.getReferenceById(userDetails.getId());
        ChatMessage chatMessage = ChatMessage.create(request.getContent(), channel, siteUser, ChatMessageType.TALK);
        return ChatMessageResponse.from(chatMessageRepository.save(chatMessage));
    }
}
