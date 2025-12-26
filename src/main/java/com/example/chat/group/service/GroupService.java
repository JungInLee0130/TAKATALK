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
import com.example.chat.group.dto.ChannelGroupResponse;
import com.example.chat.group.dto.GroupResponse;
import com.example.chat.group.dto.GroupFormRequest;
import com.example.chat.group.entity.Groups;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.groupmember.domain.GroupRole;
import com.example.chat.groupmember.entity.GroupMember;
import com.example.chat.groupmember.repository.GroupMemberRepository;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import com.example.chat.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Transactional
    public Long createGroup(CustomUserDetails userDetails, GroupFormRequest request) throws IOException {
        SiteUser siteUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 1. 닉네임 저장 그룹 생성
        Groups group = Groups.builder()
                .name(request.name())
                .build();

        GroupMember groupMember = GroupMember.builder()
                .role(GroupRole.OWNER)
                .siteUser(siteUser)
                .group(group)
                .build();
        
        // 2. 프로필이 null이 아니면 업데이트
        if (request.profile() != null && !request.profile().isEmpty()) {
            String savedFileName = fileService.storeFile(request.profile(), null);
            group.updateProfile(savedFileName);
        }

        // 3. 그룹 DB 저장
        groupRepository.save(group);
        groupMemberRepository.save(groupMember);

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

    public Groups findById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public String getInviteCode(Long groupId, Long siteUserId) {
        // 권한 체크 : 방장이나 관리자만 확인가능
        validateAdminRole(groupId, siteUserId);

        Groups group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
        return group.getInviteCode();
    }

    // 권한 체크 공통로직
    private void validateAdminRole(Long groupId, Long siteUserId) {
        GroupMember member = groupMemberRepository.findByGroupIdAndSiteUserId(groupId, siteUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_MEMBER_NOT_FOUND));

        if (member.getRole() != GroupRole.OWNER
                && member.getRole() != GroupRole.ADMIN) {
            throw new CustomException(ErrorCode.GROUP_PERMISSION_DENIED);
        }
    }

    public GroupResponse getGroupInfo(Long groupId) {
        Groups group = findById(groupId);
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .profile(group.getProfile())
                .build();
    }

    public String resetInviteCode(Long groupId, Long siteUserId) {
        // 권한 체크 : 방장이나 관리자만 확인가능
        validateAdminRole(groupId, siteUserId);

        Groups group = findById(groupId);
        group.generateInviteCode();
        return group.getInviteCode();
    }

    @Transactional
    public void deleteGroup(Long groupId, Long siteUserId) {
        // 1. 수정과 삭제는 권한 검증
        validateAdminRole(groupId, siteUserId);
        // 2. 삭제
        groupRepository.deleteById(groupId);
    }

    @Transactional
    public void editGroup(CustomUserDetails userDetails, Long groupId, GroupFormRequest request) throws IOException{
        // 1. 수정과 삭제는 권한 검증
        validateAdminRole(groupId, userDetails.getId());
        // 2. 그룹 수정
        Groups group = findById(groupId);   // 영속성 컨텍스트에 저장
        
        group.updateName(request.name());
        // 프로필 이미지가 null 이면 null 저장 (기본 이미지 적용)
        String updatedFileName = fileService.storeFile(request.profile(), group.getProfile());
        group.updateProfile(updatedFileName);
    }
}
