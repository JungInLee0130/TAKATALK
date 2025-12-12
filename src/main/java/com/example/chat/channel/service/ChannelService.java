package com.example.chat.channel.service;

import com.example.chat.category.entity.Categories;
import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.channel.domain.ChannelType;
import com.example.chat.channel.dto.ChannelResponse;
import com.example.chat.channel.dto.CreateChannelRequest;
import com.example.chat.channel.dto.ChatResponse;
import com.example.chat.channel.dto.InviteChannelResponse;
import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.entity.Chats;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.channel.repository.ChatRepository;
import com.example.chat.friends.FriendService;
import com.example.chat.friends.FriendsResponse;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.GroupRepository;
import com.example.chat.group.Groups;
import com.example.chat.visitor.repository.VisitorsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final ChatRepository chatRepository;
    private final VisitorsRepository visitorsRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private final FriendService friendService;

    @Transactional
    public void createChannel(Long categoryId, Long groupId, CreateChannelRequest request) {
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
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "카테고리 없음."));
            channel.setCategories(category);
        }

        channelRepository.save(channel);
    }

    public ChatResponse enterChannel(Long channelId) {
        Channels channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "채널이 없음."));

        // 채널 response 생성
        ChatResponse response = new ChatResponse(channel);

        Optional<Chats> chat = chatRepository.findByChannelId(channelId);

        // 채팅이 있으면  : set
        if (chat.isPresent()) {
            response.setChat(chat.get());
        }

        return response;
    }

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
}
