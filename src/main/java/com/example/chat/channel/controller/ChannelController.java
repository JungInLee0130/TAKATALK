package com.example.chat.channel.controller;

import com.example.chat.channel.dto.ChannelResponse;
import com.example.chat.channel.dto.ChatResponse;
import com.example.chat.channel.dto.CreateChannelRequest;
import com.example.chat.channel.dto.InviteChannelResponse;
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
    * 채널입장
    * */
    @GetMapping("/enter/{channelId}")
    public String enterChannel(@PathVariable(required = false) Long channelId, Model model) {
        ChatResponse chatResponse = channelService.enterChannel(channelId);
        model.addAttribute("channel", chatResponse.getChannel());
        model.addAttribute("visitors", chatResponse.getVisitorsList());
        model.addAttribute("chat", chatResponse.getChat());
        return "/chat/chatList";
    }

    /*
     * 채널 생성
     * */
    //{/channel/create(groupId = ${group})
    @PostMapping(value = "/create", produces = "application/string;charset=UTF-8") // UTF-8 설정
    public String createChannel(@RequestParam Long groupId,
                                @RequestParam(required = false) Long categoryId,
                                @ModelAttribute(name = "request") CreateChannelRequest request,
                                RedirectAttributes redirectAttributes){
        channelService.createChannel(categoryId, groupId, request);
        redirectAttributes.addAttribute("groupId", groupId);
        return "redirect:/group/access/{groupId}";
    }
    /*@PostMapping(value = "/createChannel", produces = "application/string;charset=UTF-8") // UTF-8 설정
    public ResponseEntity<String> createChannel(@RequestBody CreateChannelRequest request) {
        channelService.createChannel(request);
        return ResponseEntity.ok("SUCCESS");
    }*/

    /*
    * 채널 목록
    * */
    @GetMapping("/list")
    public List<ChannelResponse> getChannels(Long groupId) {
        return channelService.getChannels(groupId);
    }
}
