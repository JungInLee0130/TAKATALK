package com.example.chat.channel.service;

import com.example.chat.category.entity.Category;
import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.channel.dto.*;
import com.example.chat.channel.entity.Channel;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.group.entity.Group;
import com.example.chat.groupmember.domain.GroupRole;
import com.example.chat.groupmember.domain.annotation.RequireGroupRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;

    @RequireGroupRole(GroupRole.OWNER)  // OWNER 계급만 생성가능
    @Transactional
    public ChannelCreateResponse createChannel(Long groupId, Long categoryId, CreateChannelRequest request) {
        Group group = groupRepository.getReferenceById(groupId);

        Channel channel = Channel.create(request.channelName(), request.channelType(), request.isSecret(), group);
        if (categoryId != null) {
            Category category = categoryRepository.getReferenceById(categoryId);
            channel.updateCategories(category);
        }

        Channel savedChannel = channelRepository.save(channel);
        return ChannelCreateResponse.from(savedChannel);
    }

    /*카테고리 상관없이 모든 채널 가져옴*/
    public List<ChannelResponse> getChannels(Long groupId) {
        List<Channel> channels = channelRepository.findByGroupId(groupId);
        return channels.stream()
                .map(ChannelResponse::from)
                .collect(Collectors.toList());
    }

    public List<ChannelResponse> getUncategorizedChannels(Long groupId) {
        return channelRepository.findByGroupIdAndCategoryIsNull(groupId)
                .stream()
                .map(ChannelResponse::from)
                .toList();
    }

    public Channel findById(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
        return channel;
    }

    public ChannelResponse getChannelInfo(Long channelId) {
        Channel channel = findById(channelId);
        return ChannelResponse.from(channel);
    }

    @RequireGroupRole(GroupRole.OWNER)
    @Transactional
    public ChannelEditResponse editChannel(Long groupId, Long categoryId, Long channelId, ChannelEditRequest request) {
        Channel channel = findById(channelId);
        Group group = groupRepository.getReferenceById(groupId);
        if (categoryId != null) {
            Category category = categoryRepository.getReferenceById(categoryId);
            channel.updateChannel(group, category, request);
        } else {
            channel.updateChannel(group, null, request);
        }

        return ChannelEditResponse.from(channel);
    }

    @RequireGroupRole(GroupRole.OWNER)
    @Transactional
    public void deleteChannel(Long groupId, Long channelId) {
        channelRepository.deleteById(channelId);
    }
}
