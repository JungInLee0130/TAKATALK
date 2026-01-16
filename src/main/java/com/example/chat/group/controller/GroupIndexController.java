package com.example.chat.group.controller;

import com.example.chat.group.dto.GroupChannelResponse;
import com.example.chat.group.dto.GroupResponse;
import com.example.chat.group.service.GroupService;
import com.example.chat.groupmember.service.GroupMemberService;
import com.example.chat.user.dto.UserResponse;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupIndexController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final UserService userService;

    /*
     * 그룹 접속
     * */
    @GetMapping("/access/{groupId}")
    public String accessGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable(name = "groupId") Long currentGroupId,
                              Model model) {
        // 현재 그룹 정보
        GroupResponse currentGroup = groupService.getGroupInfo(currentGroupId);
        // 그룹 채널들
        GroupChannelResponse groupChannelResponse = groupService.getGroupChannelStructure(currentGroupId);
        // 자신이 속한 그룹 리스트
        List<GroupResponse> groups = groupMemberService.getGroupList(userDetails.getId());

        UserResponse user = userService.getUserDetails(userDetails.getId());

        model.addAttribute("currentGroup", currentGroup);
        model.addAttribute("groupChannelResponse", groupChannelResponse);
        model.addAttribute("groups", groups);
        model.addAttribute("user", user);

        return "channel/channel";
    }
}
