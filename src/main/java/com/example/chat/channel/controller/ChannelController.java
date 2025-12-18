package com.example.chat.channel.controller;

import com.example.chat.channel.dto.*;
import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.group.ChannelGroupResponse;
import com.example.chat.group.GroupGetResponse;
import com.example.chat.group.Groups;
import com.example.chat.group.service.GroupService;
import com.example.chat.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;
    private final GroupService groupService;

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
    public ResponseEntity<Void> createChannel(@RequestParam(name = "groupId") Long groupId,
                                                @RequestParam(required = false, name = "categoryId") Long categoryId,
                                                @RequestBody CreateChannelRequest request){
        ChannelCreateResponse response = channelService.createChannel(categoryId, groupId, request);
        return ResponseEntity.created(URI.create("/channel/enter/" + response.getChannelId() + "?groupId=" + response.getGroupId())).build();
    }

    /*
    * 채널입장
    * */
    @GetMapping("/enter/{channelId}")
    public String enterChannel(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable(name = "channelId") Long channelId,
                               @RequestParam(name = "groupId") Long currentGroupId,
                               Model model) {
        List<GroupGetResponse> groups = groupService.findAll(userDetails.getId());

        ChannelGroupResponse channelGroupResponse = groupService.accessGroup(currentGroupId);
        Groups currentGroup = groupService.findById(currentGroupId);

        Channels currentChannel = channelService.findById(channelId);

        List<ChatMessageResponse> chatMessageResponseList = channelService.enterChannel(channelId);

        model.addAttribute("currentGroup", currentGroup);
        model.addAttribute("channelGroupResponse", channelGroupResponse);
        model.addAttribute("groups", groups);

        model.addAttribute("currentChannel", currentChannel);
        // chatMessage 전달
        model.addAttribute("chatMessageResponseList", chatMessageResponseList);
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
