package com.example.chat.channel.service;

import com.example.chat.channel.domain.ChatMessages;
import com.example.chat.channel.dto.ChatMessageRequest;
import com.example.chat.channel.dto.ChatMessageResponse2;
import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.repository.ChatMessageRepository;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    public ChatMessageResponse2 save(CustomUserDetails userDetails, ChatMessageRequest request) {
        Channels channel = channelService.findById(request.getChannelId()); // 쿼리 1번

        SiteUser siteUser = userService.findById(userDetails.getId());  // 쿼리 x. 실제로 id값을 제외한 다른 value를 조회할때 쿼리나감.

        ChatMessages chatMessages = ChatMessages.builder()
                .content(request.getContent())
                .channel(channel)   // 중간에 누군가가 채널을 삭제할수있기때문에 직접 불러와야함.
                .siteUser(siteUser)
                .build();

        ChatMessages savedMessage = chatMessageRepository.save(chatMessages);

        return ChatMessageResponse2.builder()
                .channelId(channel.getId())
                .profile(userDetails.getProfile())  // 사용자는 세션값
                .nickname(userDetails.getNickname())
                .content(savedMessage.getContent())
                .createdAt(savedMessage.getCreatedAt()) // chatMessage에서 저장하고 받아와서 정확한 시간 기록
                .isModified(savedMessage.getIsModified())
                .build();
    }
}
