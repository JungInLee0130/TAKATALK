package com.example.chat.group.controller;

import com.example.chat.group.ChannelGroupResponse;
import com.example.chat.group.GroupGetResponse;
import com.example.chat.group.Groups;
import com.example.chat.group.createGroupRequest;
import com.example.chat.group.service.GroupService;
import com.example.chat.user.dto.UserResponse;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupIndexController {

    private final GroupService groupService;
    private final UserService userService;

    /*
     * 그룹 접속
     * */
    @GetMapping("/access/{groupId}")
    public String accessGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable(name = "groupId") Long currentGroupId,
                              Model model) {
        // 그룹 접속
        Groups currentGroup = groupService.findById(currentGroupId);
        // GroupResponse : 기존 채널들 response
        ChannelGroupResponse channelGroupResponse = groupService.accessGroup(currentGroupId);
        // 전체 그룹 리스트
        // GroupResponse2 : groupId, profile, name
        List<GroupGetResponse> groups = groupService.findAll(userDetails.getId());

        UserResponse user = userService.getUserDetails(userDetails.getId());

        model.addAttribute("user", user);
        model.addAttribute("currentGroup", currentGroup);
        model.addAttribute("channelGroupResponse", channelGroupResponse);
        model.addAttribute("groups", groups);

        return "channel/channel";
    }

    /*
    * 그룹만들기 페이지로 이동
    * */
    @GetMapping("/createPage")
    public String createGroupPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @ModelAttribute(name = "request") createGroupRequest request,
                                  Model model) {
        List<GroupGetResponse> groups = groupService.findAll(userDetails.getId());
        if (groups != null) {
            model.addAttribute("groups", groups);
        }
        return "group/group";
    }

}
