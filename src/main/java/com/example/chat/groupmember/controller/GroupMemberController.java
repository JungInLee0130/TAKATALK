package com.example.chat.groupmember.controller;

import com.example.chat.groupmember.service.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/visitors")
public class GroupMemberController {
    private final GroupMemberService groupMemberService;

    /*@GetMapping
    public String enterChannel(@RequestParam Long visitorsId, @RequestParam Long channelId) {
        visitorsService.enterChannel(visitorsId, channelId);
    }*/
}
