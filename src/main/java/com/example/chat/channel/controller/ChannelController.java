package com.example.chat.channel.controller;

import com.example.chat.channel.dto.*;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    /*
    * 초대버튼 클릭시
    * */
    @GetMapping("/invite-channel")
    public Model inviteChannelModalPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   @RequestParam Long channelId,
                                   Model model) {
        InviteChannelResponse response = channelService.getInviteResponse(userDetails.getId(), channelId);
        model.addAttribute("inviteChannelResponse", response);
        return model;
    }

    /*
     * 채널 생성
     * */
    @PostMapping(value = "/create", produces = "application/string;charset=UTF-8") // UTF-8 설정
    public String createChannel(@RequestParam Long groupId,
                                @RequestParam(required = false) Long categoryId,
                                @ModelAttribute(name = "request") CreateChannelRequest request,
                                RedirectAttributes redirectAttributes){
        channelService.createChannel(categoryId, groupId, request);
        redirectAttributes.addAttribute("groupId", groupId);
        return "redirect:/group/access/{groupId}";
    }

    /*
    * 채널입장
    * */
    @GetMapping("/enter/{channelId}")
    public String enterChannel(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable(name = "channelId") Long channelId,
                               Model model) {
        String channelName = channelService.findById(channelId);
        List<ChatMessageResponse> responses = channelService.enterChannel(userDetails.getId(), channelId);
        model.addAttribute("channelName", channelName);
        model.addAttribute("responses", responses);
        //model.addAttribute("visitors", chatResponse.getVisitorsList());
        return "channel/channel";
    }

    /*@GetMapping("/enter/{channelId}")
    public String enterChannel(@PathVariable(name = "channelId") Long channelId, Model model) {
        ChatResponse chatResponse = channelService.enterChannel(channelId);
        model.addAttribute("channel", chatResponse.getChannel());
        model.addAttribute("visitors", chatResponse.getVisitorsList());
        model.addAttribute("chat", chatResponse.getChat());
        return "/chat/chatList";
    }*/

    /*
    * 채널 목록
    * */
    @GetMapping("/list")
    public List<ChannelResponse> getChannels(Long groupId) {
        return channelService.getChannels(groupId);
    }
}
