package com.example.chat.groupmember.service;

import com.example.chat.channel.entity.Channel;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.dto.GroupResponse;
import com.example.chat.group.entity.Group;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.groupmember.domain.GroupRole;
import com.example.chat.groupmember.entity.GroupMember;
import com.example.chat.groupmember.repository.GroupMemberRepository;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    /*그룹 리스트 리스트 가져오기*/
    @Transactional(readOnly = true)
    public List<GroupResponse> getGroupList(Long siteUserId) {
        List<Group> groupList = groupMemberRepository.findGroupBySiteUserId(siteUserId);

        return groupList.stream()
                .map(GroupResponse::from)
                .collect(Collectors.toList());
    }

    /*그룹 멤버 등록*/
    public void joinGroup(String inviteCode, Long siteUserId){
        // 1. 그룹 찾기
        Group group = groupRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        if (groupMemberRepository.existsByGroupIdAndSiteUserId(group.getId(), siteUserId)) {
            throw new IllegalStateException("이미 가입된 서버입니다.");
        }

        createAndSaveGroupMember(siteUserId, group, GroupRole.USER);
    }

    @Transactional
    public void createAndSaveGroupMember(Long siteUserId, Group group, GroupRole role) {
        SiteUser siteUser = userRepository.findById(siteUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        GroupMember groupMember = GroupMember.create(siteUser, group, role);

        groupMemberRepository.save(groupMember);
    }

    public boolean existsBySiteUserId(Long siteUserId) {
        return groupMemberRepository.existsById(siteUserId);
    }

    public GroupMember findById(Long siteUserId) {
        return groupMemberRepository.findById(siteUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_PERMISSION_DENIED));
    }

    @Transactional(readOnly = true)
    public GroupMember getGroupMemberWithChannelId(Long siteUserId, Long channelId) {
        return groupMemberRepository.getGroupMemberWithChannelId(siteUserId, channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_MEMBER_NOT_FOUND));
    }

    public GroupMember findByGroupIdAndSiteUserId(Long groupId, Long siteUserId) {
        return groupMemberRepository.findByGroupIdAndSiteUserId(groupId, siteUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_MEMBER_NOT_FOUND));
    }

    /*public InviteChannelResponse getInviteResponse(Long siteUserid, Long channelId) {
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
    }*/

    /*public void enterChannel(Long visitorsId, Long channelId) {
        Visitors visitor = visitorsRepository.findById(visitorsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        visitor.getChannels().getId().equals(channelId);
    }*/
}
