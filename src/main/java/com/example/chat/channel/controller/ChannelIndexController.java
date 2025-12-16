package com.example.chat.channel.controller;

import com.example.chat.channel.dto.CreateChannelRequest;
import com.example.chat.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelIndexController {
    private final GroupService groupService;

    /*
    * 채널생성 페이지
    * */
    @GetMapping("/create")
    public String channelCreatePage(@RequestParam(name = "groupId") Long groupId,
                                    @RequestParam(name = "categoryId", required = false) Long categoryId,
                                    Model model) {
        model.addAttribute("groupId", groupId);
        if (categoryId != null) {
            model.addAttribute("categoryId", categoryId);
        }
        return "channel/channel-create";
    }
}
