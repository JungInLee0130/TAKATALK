package com.example.chat.groupmember.service;

import com.example.chat.channel.dto.InviteChannelResponse;
import com.example.chat.channel.entity.Channel;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.friends.FriendsResponse;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.dto.GroupJoinRequest;
import com.example.chat.group.dto.GroupResponse;
import com.example.chat.group.entity.Group;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.group.service.GroupService;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final UserService userService;

    /*그룹 리스트 리스트 가져오기*/
    public List<GroupResponse> getGroupList(Long siteUserId) {
        List<Group> groupList = groupMemberRepository.findGroupBySiteUserId(siteUserId);

        return groupList.stream()
                .map(GroupResponse::from)
                .toList();
    }

    /*그룹 멤버 등록*/
    @Transactional
    public void joinGroup(String inviteCode, Long siteUserId){
        Group group = groupRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND)); // 그룹 존재 여부. 프론트에서 이 코드 하나로 그룹에 가입함.
        if (groupMemberRepository.existsByGroupIdAndSiteUserId(group.getId(), siteUserId)) {
            throw new CustomException(ErrorCode.ALREADY_JOINED_GROUP);              // 1. 이미 그룹 멤버인경우
        }
        group.validateInviteCode(inviteCode);         // 2. 그룹 초대코드가 유효하지않은경우

        createAndSaveGroupMember(siteUserId, group, GroupRole.USER);
    }

    // 그룹 멤버: 생성, 저장*/
    private void createAndSaveGroupMember(Long siteUserId, Group group, GroupRole role) {
        SiteUser siteUser = userService.findById(siteUserId);
        GroupMember groupMember = GroupMember.create(siteUser, group, role);
        groupMemberRepository.save(groupMember);
    }

    // 채널ID, 유저 ID를 이용하여 그룹멤버 찾기
    public GroupMember getGroupMemberWithChannelId(Long siteUserId, Long channelId) {
        return groupMemberRepository.getGroupMemberWithChannelId(siteUserId, channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_MEMBER_NOT_FOUND));
    }
}
