package com.example.chat.group.service;

import com.example.chat.category.entity.Categories;
import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.category.dto.CategoryResponse;
import com.example.chat.channel.dto.ChannelResponse;
import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.global.file.FileService;
import com.example.chat.group.*;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.visitor.repository.VisitorsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Transactional
    public Long createGroup(CustomUserDetails userDetails, createGroupRequest request) throws IOException {
        SiteUser siteUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 1. 닉네임 저장 그룹 생성
        Groups group = Groups.builder()
                .name(request.name())
                .siteUser(siteUser)
                .build();
        
        // 2. 프로필이 null이 아니면 업데이트
        if (request.profile() != null && !request.profile().isEmpty()) {
            String savedFileName = fileService.storeFile(request.profile());
            group.updateProfileImageUrl(savedFileName);
        }

        // 3. 그룹 DB 저장
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
                        group.getProfileImageUrl()))
                .collect(Collectors.toList());
    }

    public Groups findById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
    }
}
