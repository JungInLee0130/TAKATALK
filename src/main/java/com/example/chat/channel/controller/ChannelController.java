package com.example.chat.channel.controller;

import com.example.chat.channel.dto.*;
import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.channel.service.ChatMessageService;
import com.example.chat.group.dto.ChannelGroupResponse;
import com.example.chat.group.dto.GroupGetResponse;
import com.example.chat.group.entity.Groups;
import com.example.chat.group.service.GroupService;
import com.example.chat.user.dto.UserResponse;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
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
    private final ChatMessageService chatMessageService;

    private final UserService userService;

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

        List<ChatMessageResponse> chatMessageResponseList = chatMessageService.getOldMessage(channelId, null);

        UserResponse user = userService.getUserDetails(userDetails.getId());

        model.addAttribute("user", user);
        model.addAttribute("currentGroup", currentGroup);
        model.addAttribute("channelGroupResponse", channelGroupResponse);
        model.addAttribute("groups", groups);

        model.addAttribute("currentChannel", currentChannel);
        // chatMessage 전달
        model.addAttribute("chatMessageResponseList", chatMessageResponseList);

        return "channel/channel";
    }

    /*
    * 채널 목록
    * */
    @GetMapping("/list")
    public List<ChannelResponse> getChannels(Long groupId) {
        return channelService.getChannels(groupId);
    }
}
