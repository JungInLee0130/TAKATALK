package com.example.chat.chatmessage.service;

import com.example.chat.channel.entity.Channel;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.chatmessage.domain.ChatMessageType;
import com.example.chat.chatmessage.dto.ChatMessageResponse;
import com.example.chat.chatmessage.entity.ChatMessage;
import com.example.chat.chatmessage.repository.ChatMessageRepository;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChannelService channelService;
    private final UserService userService;

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

    @Transactional  // 안붙여도 영속성보장이되긴하는데, 커넥션을 findById 할때마다 계속 반납하고 주고함. 한번에 처리, 그리고 역시 원자성보장.
    public ChatMessage saveSystemMessage(Long channelId, Long siteUserId, String content, ChatMessageType type) {
        Channel channel = channelService.findById(channelId);
        SiteUser siteUser = userService.findById(siteUserId);
        ChatMessage welcomeMsg = ChatMessage.create(content, channel, siteUser, type);
        return chatMessageRepository.save(welcomeMsg);
    }
}
