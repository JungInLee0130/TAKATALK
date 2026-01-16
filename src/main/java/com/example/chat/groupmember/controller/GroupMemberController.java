package com.example.chat.groupmember.controller;

import com.example.chat.group.dto.GroupJoinRequest;
import com.example.chat.groupmember.service.GroupMemberService;
import com.example.chat.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group-member")
public class GroupMemberController {
    private final GroupMemberService groupMemberService;

    /*
     * 그룹 초대
     * */
    @PostMapping("/join")
    public ResponseEntity<Void> joinGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody GroupJoinRequest request) {
        groupMemberService.joinGroup(request.getInviteCode(), userDetails.getId());
        return ResponseEntity.ok().build();
    }

    /*@GetMapping("/invite-channel")
    public Model inviteChannelModalPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestParam Long channelId,
                                        Model model) {
        InviteChannelResponse response = channelService.getInviteResponse(userDetails.getId(), channelId);
        model.addAttribute("inviteChannelResponse", response);
        return model;
    }*/
}
