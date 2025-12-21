package com.example.chat.login.controller;

import com.example.chat.group.dto.GroupGetResponse;
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

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final UserService userService;

    @GetMapping("/index")
    public String index(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                        Model model) {
        model.addAttribute("nickname", customUserDetails.getNickname());
        return "index";
    }

    /*
    * 메인페이지 이동
    * */
    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  Model model) {
        //List<GroupGetResponse> groups = groupService.findAll(userDetails.getId());
        UserResponse user = userService.getUserDetails(userDetails.getId());
        List<GroupGetResponse> groupList = groupMemberService.getGroupList(userDetails.getId());

        model.addAttribute("groups", groupList);
        model.addAttribute("user", user);

        return "channel/channel";
    }
}
