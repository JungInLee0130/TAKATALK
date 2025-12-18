package com.example.chat.channel.service;

import com.example.chat.category.entity.Categories;
import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.channel.entity.ChatMessages;
import com.example.chat.channel.dto.*;
import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.channel.repository.ChatMessageRepository;
import com.example.chat.friends.FriendService;
import com.example.chat.friends.FriendsResponse;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.GroupRepository;
import com.example.chat.group.Groups;
import com.example.chat.user.repository.UserRepository;
import com.example.chat.visitor.repository.VisitorsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final VisitorsRepository visitorsRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private final FriendService friendService;
    private final UserRepository userRepository;

    @Transactional
    public ChannelCreateResponse createChannel(Long categoryId, Long groupId, CreateChannelRequest request) {
        Groups group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Channels channel = Channels.builder()
                .type(request.channelType())
                .name(request.channelName())
                .isSecret(request.isSecret())
                .group(group)
                .build();

        if (categoryId != null) {
            Categories category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
            channel.setCategories(category);
        }

        channelRepository.save(channel);

        return ChannelCreateResponse.builder()
                .channelId(channel.getId())
                .groupId(groupId)
                .build();
    }



    public List<ChatMessageResponse> enterChannel(Long channelId) {
        List<ChatMessages> chatMessages = chatMessageRepository.findAllByChannelId(channelId);

        List<ChatMessageResponse> responses = new ArrayList<>();
        for (ChatMessages chatMessage: chatMessages) {
            ChatMessageResponse response = ChatMessageResponse.builder()
                    .profile(chatMessage.getSiteUser().getProfile())
                    .nickname(chatMessage.getSiteUser().getNickname())
                    .isModified(chatMessage.getIsModified())
                    .channelId(chatMessage.getChannel().getId())
                    .content(chatMessage.getContent())
                    .createdAt(chatMessage.getCreatedAt())
                    .build();

            responses.add(response);
        }

        return responses;
    }

    /*public ChatResponse enterChannel(Long channelId) {

        // 채널 response 생성
        ChatResponse response = new ChatResponse(channel);

        Optional<Chats> chat = chatRepository.findByChannelId(channelId);

        // 채팅이 있으면  : set
        if (chat.isPresent()) {
            response.setChat(chat.get());
        }

        return response;
    }*/

    public InviteChannelResponse getInviteResponse(Long siteUserid, Long channelId) {
        Channels channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        String groupName = channel.getGroup().getName();
        String channelName = channel.getName();
        List<FriendsResponse> friendsResponses = friendService.getFriendsResponses(siteUserid);

        return InviteChannelResponse.builder()
                .groupName(groupName)
                .channelName(channelName)
                .friendsResponses(friendsResponses)
                .build();
    }

    public List<ChannelResponse> getChannels(Long groupId) {
        List<Channels> channels = channelRepository.findByGroupId(groupId);

        return channels.stream()
                .map(channel -> new ChannelResponse(channel.getId(),
                        channel.getName(),
                        channel.getType(),
                        channel.getIsSecret()))
                .collect(Collectors.toList());
    }

    public Channels findById(Long channelId) {
        Channels channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
        return channel;
    }
}
