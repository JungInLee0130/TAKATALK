package com.example.chat.group.controller;

import com.example.chat.group.dto.GroupJoinRequest;
import com.example.chat.groupmember.service.GroupMemberService;
import com.example.chat.user.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "GroupMember Api", description = "그룹멤버와 관련된 Api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group-member")
public class ApiGroupMemberController {
    private final GroupMemberService groupMemberService;

    @Operation(summary = "그룹 멤버로 참여", description = "초대 코드를 입력하여 그룹 멤버로 참여합니다.")
    @PostMapping("/join")
    public ResponseEntity<Void> joinGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @Valid @RequestBody GroupJoinRequest request) {
        groupMemberService.joinGroup(request.getInviteCode(), userDetails.getId());
        return ResponseEntity.ok().build();
    }
}
