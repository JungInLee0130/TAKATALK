package com.example.chat.group.service;

import com.example.chat.category.dto.CategoryResponse;
import com.example.chat.category.service.CategoryService;
import com.example.chat.channel.dto.ChannelResponse;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.global.file.FileService;
import com.example.chat.group.dto.GroupChannelResponse;
import com.example.chat.group.dto.GroupEditResponse;
import com.example.chat.group.dto.GroupFormRequest;
import com.example.chat.group.dto.GroupResponse;
import com.example.chat.group.entity.Group;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.groupmember.domain.GroupRole;
import com.example.chat.groupmember.domain.annotation.RequireGroupRole;
import com.example.chat.groupmember.entity.GroupMember;
import com.example.chat.groupmember.repository.GroupMemberRepository;
import com.example.chat.groupmember.service.GroupMemberService;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    private final UserService userService;
    private final CategoryService categoryService;
    private final ChannelService channelService;
    private final FileService fileService;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;


    public Group findById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
    }


    @Transactional
    public Long createGroup(CustomUserDetails userDetails, GroupFormRequest request) throws IOException {
        SiteUser siteUser = userService.findById(userDetails.getId());
        // 1. 닉네임 저장 그룹 생성
        Group group = Group.create(request.name());
        GroupMember groupMember = GroupMember.create(siteUser, group, GroupRole.OWNER);

        // 2. 프로필이 null이 아니면 업데이트
        if (request.profile() != null && !request.profile().isEmpty()) {
            String savedFileName = fileService.storeFile(request.profile(), null);
            group.updateProfile(savedFileName);
        }

        // 3. 그룹 DB 저장
        groupRepository.save(group);
        groupMemberRepository.save(groupMember);  // OWNER로 저장

        return group.getId();
    }

    /*카테고리 채널 + 카테고리 없는 채널 모두 가져온다.*/
    @Transactional(readOnly = true)
    public GroupChannelResponse getGroupChannelStructure(Long groupId) {
        List<CategoryResponse> categorizedChannels = categoryService.getCategorizedChannels(groupId);
        List<ChannelResponse> uncategorizedChannels = channelService.getUncategorizedChannels(groupId);
        return GroupChannelResponse.of(categorizedChannels, uncategorizedChannels);
    }
    @RequireGroupRole(GroupRole.OWNER) // 권한 체크 : 방장이나 관리자만 확인가능
    @Transactional(readOnly = true)
    public String getInviteCode(Long groupId) {
        Group group = findById(groupId);
        return group.getInviteCode();
    }

    public GroupResponse getGroupInfo(Long groupId) {
        Group group = findById(groupId);
        return GroupResponse.from(group);
    }

    /* 그룹 초대코드 초기화 */
    @RequireGroupRole(GroupRole.OWNER) // 권한 체크 : 방장이나 관리자만 확인가능
    @Transactional
    public String resetInviteCode(Long groupId) {
        Group group = findById(groupId);
        group.updateInviteCode();
        return group.getInviteCode();
    }

    /* 그룹 삭제 */
    @RequireGroupRole(GroupRole.OWNER) // 1. 수정과 삭제는 권한 검증 : CustomAnnotation + AOP
    @Transactional
    public void deleteGroup(Long groupId) {
        // 2. 삭제
        groupRepository.deleteById(groupId);
    }

    /* 그룹 수정 */
    @RequireGroupRole(GroupRole.OWNER) // 1. 수정과 삭제는 권한 검증
    @Transactional
    public GroupEditResponse editGroup(Long groupId, GroupFormRequest request) throws IOException {
        // 2. 그룹 수정
        Group group = findById(groupId);   // 영속성 컨텍스트에 저장
        group.updateName(request.name());
        String updatedFileName = fileService.storeFile(request.profile(), group.getProfile());
        group.updateProfile(updatedFileName);

        return GroupEditResponse.from(group);
    }
}
