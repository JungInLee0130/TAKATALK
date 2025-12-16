package com.example.chat.group.service;

import com.example.chat.category.entity.Categories;
import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.category.dto.CategoryResponse;
import com.example.chat.channel.dto.ChannelResponse;
import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.*;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.visitor.repository.VisitorsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ChannelRepository channelRepository;

    private final VisitorsRepository visitorsRepository;
    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long createGroup(CustomUserDetails userDetails, createGroupRequest request) {
        SiteUser siteUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Groups group = new Groups(request.name(), siteUser);

        if (StringUtils.hasText(request.profile())) {
            group.setProfile(request.profile());
        }

        groupRepository.save(group);

        return group.getId();
    }

    public ChannelGroupResponse accessGroup(Long groupId) {
        Groups group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
        // 전체 카테고리 조회
        List<Categories> categories = categoryRepository.findByGroupId(groupId);

        // 전체 채널조회
        List<Channels> channels = channelRepository.findByGroupId(groupId);

        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Categories category : categories) {
            List<ChannelResponse> categorizedChannels = new ArrayList<>();
            for (Channels channel : channels) {
                if(channel.getCategories() != null) {
                    if (category.getId() == channel.getCategories().getId()) {
                        categorizedChannels.add(new ChannelResponse(
                                channel.getId(),
                                channel.getName(),
                                channel.getType(),
                                channel.getIsSecret()
                        ));
                    }
                }
            }
            categoryResponses.add(new CategoryResponse(category.getId(),
                    category.getName(), categorizedChannels));
        }

        List<ChannelResponse> uncategorizedChannels = new ArrayList<>();

        for (Channels channel : channels) {
            if (channel.getCategories() == null) {
                uncategorizedChannels.add(new ChannelResponse(channel.getId(),
                        channel.getName(),
                        channel.getType(),
                        channel.getIsSecret()));
            }
        }

        ChannelGroupResponse response = new ChannelGroupResponse(categoryResponses, uncategorizedChannels);

        // 전체 방문객 조회
        /*List<Visitors> visitors = visitorsRepository.findByGroupId(groupId);

        response.setVisitorsList(visitors);*/

        return response;
    }

    public List<GroupGetResponse> findAll(Long siteUserId) {
        List<Groups> groups = groupRepository.findAllBySiteUserId(siteUserId);

        return groups.stream()
                .map(group -> new GroupGetResponse(group.getId(),
                        group.getName(),
                        group.getProfile()))
                .collect(Collectors.toList());
    }

    public Groups findById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
    }
}
