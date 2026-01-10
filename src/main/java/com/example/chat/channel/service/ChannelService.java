package com.example.chat.channel.service;

import com.example.chat.category.entity.Category;
import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.channel.dto.*;
import com.example.chat.channel.entity.Channel;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.channel.repository.ChatMessageRepository;
import com.example.chat.friends.FriendService;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.group.entity.Group;
import com.example.chat.user.repository.UserRepository;
import com.example.chat.groupmember.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public ChannelCreateResponse createChannel(Long categoryId, Long groupId, CreateChannelRequest request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Channel channel = Channel.create(request.channelName(), request.channelType(), request.isSecret(), group);
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
            channel.updateCategories(category);
        }

        Channel savedChannel = channelRepository.save(channel);
        return ChannelCreateResponse.from(savedChannel);
    }

    public List<ChannelResponse> getChannels(Long groupId) {
        List<Channel> channels = channelRepository.findByGroupId(groupId);
        return channels.stream()
                .map(ChannelResponse::from)
                .collect(Collectors.toList());
    }

    public Channel findById(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
        return channel;
    }

    public Channel getReferenceById(Long channelId) {
        return channelRepository.getReferenceById(channelId);
    }

    public ChannelResponse getChannelInfo(Long channelId) {
        Channel channel = findById(channelId);
        return ChannelResponse.from(channel);
    }
}
