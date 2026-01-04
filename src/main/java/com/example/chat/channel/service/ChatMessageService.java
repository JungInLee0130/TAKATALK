package com.example.chat.channel.service;

import com.example.chat.channel.domain.ChatMessageType;
import com.example.chat.channel.dto.ChatMessageRequest;
import com.example.chat.channel.dto.ChatMessageResponse;
import com.example.chat.channel.entity.Channel;
import com.example.chat.channel.entity.ChatMessage;
import com.example.chat.channel.repository.ChatMessageRepository;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

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

    public List<ChatMessageResponse> getOldMessage(Long channelId, Long lastMessageId) {
        Pageable pageable = PageRequest.of(0, 20);

        Slice<ChatMessage> slice;
        if (lastMessageId == null) {
            // lastMessageId가 없으면 -> 가장 최신 메시지 ID 찾아서 조회
            slice = chatMessageRepository.findByChannelIdOrderByIdDesc(channelId, pageable);
        } else {
            // lastMessageId가 있으면 -> repository...findBy..LessThan.. 호출
            slice = chatMessageRepository.findByChannelIdAndIdLessThanOrderByIdDesc(channelId, lastMessageId, pageable);
        }

        List<ChatMessageResponse> responseList = slice.getContent().stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
        Collections.reverse(responseList);
        return responseList;
    }

    public ChatMessage saveSystemMessage(Long channelId, Long siteUserId, String content, ChatMessageType type) {
        Channel channel = channelService.findById(channelId);
        SiteUser siteUser = userService.findById(siteUserId);
        ChatMessage welcomeMsg = ChatMessage.create(content, channel, siteUser, type);
        return chatMessageRepository.save(welcomeMsg);
    }
}
