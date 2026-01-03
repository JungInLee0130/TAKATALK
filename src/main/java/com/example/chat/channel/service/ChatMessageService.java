package com.example.chat.channel.service;

import com.example.chat.channel.domain.ChatMessageType;
import com.example.chat.channel.dto.ChatMessageRequest;
import com.example.chat.channel.dto.ChatMessageResponse;
import com.example.chat.channel.entity.Channel;
import com.example.chat.channel.entity.ChatMessages;
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
        Channel channel = channelService.findById(request.getChannelId()); // 쿼리 1번

        SiteUser siteUser = userService.findById(userDetails.getId());  // 쿼리 x. 실제로 id값을 제외한 다른 value를 조회할때 쿼리나감.

        ChatMessages chatMessages = ChatMessages.builder()
                .type(ChatMessageType.TALK)
                .content(request.getContent())
                .channel(channel)   // 중간에 누군가가 채널을 삭제할수있기때문에 직접 불러와야함.
                .siteUser(siteUser)
                .build();

        ChatMessages savedMessage = chatMessageRepository.save(chatMessages);

        return ChatMessageResponse.builder()
                .type(ChatMessageType.TALK)
                .channelId(channel.getId())
                .profile(userDetails.getProfile())  // 사용자는 세션값
                .nickname(userDetails.getNickname())
                .content(savedMessage.getContent())
                .createdAt(savedMessage.getCreatedAt()) // chatMessage에서 저장하고 받아와서 정확한 시간 기록
                .isModified(savedMessage.getIsModified())
                .chatMessageId(savedMessage.getId())
                .build();
    }

    public List<ChatMessageResponse> getOldMessage(Long channelId, Long lastMessageId) {

        Pageable pageable = PageRequest.of(0, 20);

        Slice<ChatMessages> slice;

        // lastMessageId가 없으면 -> 가장 최신 메시지 ID 찾아서 조회
        // lastMessageId가 있으면 -> repository...findBy..LessThan.. 호출
        if (lastMessageId == null) {
            slice = chatMessageRepository.findByChannelIdOrderByIdDesc(channelId, pageable);
        } else {
            slice = chatMessageRepository.findByChannelIdAndIdLessThanOrderByIdDesc(channelId, lastMessageId, pageable);
        }


        List<ChatMessageResponse> responseList = slice.getContent().stream()
                .map(chatmessage -> ChatMessageResponse.builder()
                        .channelId(chatmessage.getChannel().getId())
                        .profile(chatmessage.getSiteUser().getProfile())
                        .nickname(chatmessage.getSiteUser().getNickname())
                        .content(chatmessage.getContent())
                        .createdAt(chatmessage.getCreatedAt())
                        .isModified(chatmessage.getIsModified())
                        .chatMessageId(chatmessage.getId())
                        .type(chatmessage.getType())
                        .build())
                .collect(Collectors.toList());

        Collections.reverse(responseList);

        return responseList;
    }

    public ChatMessages saveSystemMessage(Long channelId, Long siteUserId, String content, ChatMessageType type) {
        Channel channel = channelService.getReferenceById(channelId);
        SiteUser siteUser = userService.getReferenceById(siteUserId);
        ChatMessages welcomeMsg = ChatMessages.builder()
                .channel(channel)
                .siteUser(siteUser)
                .content(content)
                .type(type) // 현재는 ENTER밖에없음.
                .build();

        return chatMessageRepository.save(welcomeMsg);
    }
}
