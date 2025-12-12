package com.example.chat.group.controller;

import com.example.chat.category.service.CategoryService;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.group.GroupResponse;
import com.example.chat.group.GroupResponse2;
import com.example.chat.group.Groups;
import com.example.chat.group.service.GroupService;
import com.example.chat.group.createGroupRequest;
import com.example.chat.user.service.CustomUserDetails;
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
    private final CategoryService categoryService;
    private final ChannelService channelService;

    /*
     * 그룹 접속
     * */
    @GetMapping("/access/{groupId}")
    public String accessGroup(@PathVariable(name = "groupId") Long groupId, Model model) {
        // 그룹 접속
        Groups currentGroup = groupService.findById(groupId);
        GroupResponse groupResponse = groupService.accessGroup(groupId);

        model.addAttribute("currentGroup", currentGroup);
        model.addAttribute("groupResponse", groupResponse);

        return "channel/channel";
    }

    /*
    * 그룹만들기 페이지로 이동
    * */
    @GetMapping("/createPage")
    public String createGroupPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @ModelAttribute(name = "request") createGroupRequest request,
                                  Model model) {
        List<GroupResponse2> groups = groupService.findAll(userDetails.getId());
        if (groups != null) {
            model.addAttribute("groups", groups);
        }
        return "/group/group";
    }

}
