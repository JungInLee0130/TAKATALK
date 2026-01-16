package com.example.chat.group.controller;

import com.example.chat.group.dto.GroupChannelResponse;
import com.example.chat.group.dto.GroupEditResponse;
import com.example.chat.group.dto.GroupFormRequest;
import com.example.chat.group.dto.GroupResponse;
import com.example.chat.group.service.GroupService;
import com.example.chat.groupmember.service.GroupMemberService;
import com.example.chat.user.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Tag(name = "Group Api", description = "그룹(서버)와 관련된 Api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
public class ApiGroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;

    @Operation(summary = "그룹 리스트 조회", description = "사이드바 그룹 리스트 조회(자신이 속해있는 그룹을 조회합니다.)")
    @GetMapping("/list")
    public ResponseEntity<List<GroupResponse>> getGroupList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<GroupResponse> groups = groupMemberService.getGroupList(userDetails.getId());
        return ResponseEntity.ok(groups);
    }
    
    @Operation(summary = "그룹 단일 조회", description = "그룹 정보를 조회합니다. 예) 그룹 수정시 그룹 정보 불러옴.")
    @GetMapping("/info/{groupId}")
    public ResponseEntity<GroupResponse> getGroupInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable(name = "groupId") Long groupId) {
        GroupResponse groupInfo = groupService.getGroupInfo(groupId);
        return ResponseEntity.ok(groupInfo);
    }
    
    @Operation(summary = "그룹 생성", description = "그룹을 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<Long> createGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @Valid @RequestBody GroupFormRequest request) throws IOException {
        Long newGroupId = groupService.createGroup(userDetails, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGroupId);
    }
    
    @Operation(summary = "그룹 수정", description = "그룹을 수정합니다.(프로필, 닉네임 등)")
    @PatchMapping("/{groupId}")
    public ResponseEntity<GroupEditResponse> editGroup(@PathVariable(name = "groupId") Long groupId,
                                          @Valid @RequestBody GroupFormRequest request) throws IOException {
        GroupEditResponse response = groupService.editGroup(groupId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "그룹 삭제", description = "그룹을 삭제합니다.(soft delete)")
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable(name = "groupId") Long groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "그룹 초대코드 get (GROUPMEMBER_ROLE : OWNER)", description = "그룹 초대코드를 가져옵니다.")
    @GetMapping("/{currentGroupId}/invite-code")
    public ResponseEntity<String> getInviteCode(@PathVariable(name = "currentGroupId") Long currentGroupId) {
        String inviteCode = groupService.getInviteCode(currentGroupId);
        return ResponseEntity.ok(inviteCode);
    }

    @Operation(summary = "그룹 초대코드 초기화 (GROUPMEMBER_ROLE : OWNER)", description = "그룹 초대코드를 리셋합니다.")
    @GetMapping("/{currentGroupId}/invite-code/reset")
    public ResponseEntity<String> resetInviteCode(@PathVariable(name = "currentGroupId") Long currentGroupId) {
        String newInviteCode = groupService.resetInviteCode(currentGroupId);
        return ResponseEntity.ok(newInviteCode);
    }


    @Operation(summary = "전체 채널 반환", description = "전체 채널을 반환합니다.")
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupChannelResponse> getGroupChannelStructure(@PathVariable(name = "groupId") Long groupId) {
        GroupChannelResponse response = groupService.getGroupChannelStructure(groupId);
        return ResponseEntity.ok(response);
    }
}
