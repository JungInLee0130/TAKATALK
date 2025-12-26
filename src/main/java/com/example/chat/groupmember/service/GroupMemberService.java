package com.example.chat.groupmember.service;

import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.dto.GroupResponse;
import com.example.chat.group.entity.Groups;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.groupmember.domain.GroupRole;
import com.example.chat.groupmember.entity.GroupMember;
import com.example.chat.groupmember.repository.GroupMemberRepository;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public List<GroupResponse> getGroupList(Long siteUserId) {
        List<Groups> groupList = groupMemberRepository.findGroupBySiteUserId(siteUserId);

        return groupList.stream()
                .map(group -> GroupResponse.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .profile(group.getProfile())
                    .build())
                .collect(Collectors.toList());
    }

    public void joinGroup(String inviteCode, Long siteUserId){
        // 1. 그룹 찾기
        Groups group = groupRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        if (groupMemberRepository.existsByGroupIdAndSiteUserId(group.getId(), siteUserId)) {
            throw new IllegalStateException("이미 가입된 서버입니다.");
        }

        SiteUser siteUser = userRepository.findById(siteUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        GroupMember groupMember = GroupMember.builder()
                .siteUser(siteUser)
                .group(group)
                .role(GroupRole.USER)
                .build();

        groupMemberRepository.save(groupMember);
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
