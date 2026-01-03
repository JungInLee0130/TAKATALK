package com.example.chat.group.controller;

import com.example.chat.category.dto.CategoryResponse;
import com.example.chat.group.dto.ChannelGroupResponse;
import com.example.chat.group.dto.GroupResponse;
import com.example.chat.group.service.GroupService;
import com.example.chat.groupmember.service.GroupMemberService;
import com.example.chat.user.dto.UserResponse;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
        // 그룹 접속
        GroupResponse currentGroup = groupService.getGroupInfo(currentGroupId);
        // GroupResponse : 기존 채널들 response
        ChannelGroupResponse channelGroupResponse = groupService.accessGroup(currentGroupId);
        // 자신이 속한 그룹 리스트
        List<GroupResponse> groups = groupMemberService.getGroupList(userDetails.getId());

        UserResponse user = userService.getUserDetails(userDetails.getId());

        model.addAttribute("user", user);
        model.addAttribute("currentGroup", currentGroup);
        model.addAttribute("channelGroupResponse", channelGroupResponse);
        model.addAttribute("groups", groups);

        return "channel/channel";
    }
}
