package com.example.chat.group.controller;

import com.example.chat.group.GroupResponse;
import com.example.chat.group.GroupResponse2;
import com.example.chat.group.Groups;
import com.example.chat.group.service.GroupService;
import com.example.chat.group.createGroupRequest;
import com.example.chat.user.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    /*
    * 그룹 생성
    * */
    @PostMapping("/create")
    public String createGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @Valid @ModelAttribute(name = "request") createGroupRequest request) {
        groupService.createGroup(userDetails, request);
        return "redirect:/main";
    }

    /*
    * 그룹 리스트 조회
    * */
    @GetMapping("/list")
    public String getGroupList(Long siteUserId, Model model) {
        List<GroupResponse2> groups = groupService.findAll(siteUserId);
        model.addAttribute("groups", groups);
        return "/index";    // 메인화면
    }
}
