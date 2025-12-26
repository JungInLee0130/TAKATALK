package com.example.chat.group.controller;

import com.example.chat.group.dto.GroupResponse;
import com.example.chat.group.service.GroupService;
import com.example.chat.group.dto.GroupFormRequest;
import com.example.chat.groupmember.service.GroupMemberService;
import com.example.chat.user.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;

    /*
     * 그룹 리스트 조회
     * */
    @GetMapping("/list")
    public ResponseEntity<List<GroupResponse>> getGroupList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<GroupResponse> groups = groupMemberService.getGroupList(userDetails.getId());
        return ResponseEntity.ok(groups);
    }

    /*
     * 그룹 단일 조회
     * */
    @GetMapping("/info/{groupId}")
    public ResponseEntity<GroupResponse> getGroupInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable(name = "groupId") Long groupId) {
        GroupResponse groupInfo = groupService.getGroupInfo(groupId);
        return ResponseEntity.ok(groupInfo);
    }

    /*
    * 그룹 생성
    * */
    @PostMapping("/create")
    public ResponseEntity<Void> createGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @Valid GroupFormRequest request) throws IOException {
        Long newGroupId = groupService.createGroup(userDetails, request);
        return ResponseEntity.created(URI.create("/group/access/" + newGroupId)).build();
    }

    /*
    * 그룹 수정
    * */
    @PatchMapping("/{groupId}")
    public ResponseEntity<Void> editGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(name = "groupId") Long groupId,
                                            @Valid GroupFormRequest request) throws IOException {
        groupService.editGroup(userDetails, groupId, request);
        return ResponseEntity.ok().build();
    }

    /*
     * 그룹 삭제
     * */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(name = "groupId") Long groupId) {
        groupService.deleteGroup(groupId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    /*
    * 그룹 초대코드 get
    * */
    @GetMapping("/{currentGroupId}/invite-code")
    public ResponseEntity<String> getInviteCode(@PathVariable(name = "currentGroupId") Long currentGroupId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        String inviteCode = groupService.getInviteCode(currentGroupId, userDetails.getId());
        return ResponseEntity.ok(inviteCode);
    }

    /*
    * 그룹 초대코드 reset
    * */
    @GetMapping("/{currentGroupId}/invite-code/reset")
    public ResponseEntity<String> resetInviteCode(@PathVariable(name = "currentGroupId") Long currentGroupId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        String newInviteCode = groupService.resetInviteCode(currentGroupId, userDetails.getId());
        return ResponseEntity.ok(newInviteCode);
    }
}
