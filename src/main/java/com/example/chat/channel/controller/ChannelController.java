package com.example.chat.channel.controller;

import com.example.chat.channel.dto.*;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.channel.service.ChatMessageService;
import com.example.chat.group.dto.ChannelGroupResponse;
import com.example.chat.group.dto.GroupResponse;
import com.example.chat.group.service.GroupService;
import com.example.chat.groupmember.service.GroupMemberService;
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
    private final GroupMemberService groupMemberService;
    private final ChatMessageService chatMessageService;

    private final UserService userService;

    /*
     * 채널 생성
     * */
    @PostMapping(value = "/create", produces = "application/string;charset=UTF-8") // UTF-8 설정
    public ResponseEntity<Void> createChannel(@RequestBody CreateChannelRequest request){
        ChannelCreateResponse response = channelService.createChannel(request.categoryId(), request.groupId(), request);
        return ResponseEntity.created(URI.create("/channel/" + response.getChannelId())).build();
    }
    /*
    * 채널입장
    * */
    /*@GetMapping("/enter/{channelId}")
    public String enterChannel(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable(name = "channelId") Long channelId,
                               @RequestParam(name = "groupId") Long currentGroupId,
                               Model model) {
        List<GroupResponse> groups = groupMemberService.getGroupList(userDetails.getId());

        ChannelGroupResponse channelGroupResponse = groupService.accessGroup(currentGroupId);
        GroupResponse currentGroup = groupService.getGroupInfo(currentGroupId);

        ChannelResponse currentChannel = channelService.getChannelInfo(channelId);

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
    }*/
    /** 채널 입장 : 채널 메시지 불러오기 **/
    /*@GetMapping("/{channelId}")
    public ResponseEntity<List<ChatMessageResponse>> enterChannel(@PathVariable(name = "channelId") Long channelId) {
        List<ChatMessageResponse> chatMessageResponseList = chatMessageService.getOldMessage(channelId, null);
        return ResponseEntity.ok(chatMessageResponseList);
    }*/

    @GetMapping("/{channelId}")
    public String enterChannel(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable(name = "channelId") Long channelId, Model model) {
        // 1. 채널 정보
        ChannelResponse currentChannel = channelService.getChannelInfo(channelId);
        model.addAttribute("currentChannel", currentChannel);

        // 2. 이전 채팅 메시지
        List<ChatMessageResponse> chatMessageResponseList = chatMessageService.getOldMessage(channelId, null);
        model.addAttribute("chatMessageResponseList", chatMessageResponseList);

        // 3. 유저 정보
        UserResponse user = userService.getUserDetails(userDetails.getId());
        model.addAttribute("user", user);

        return "fragments/layout/chat-area :: chatArea";
    }

    /*
    * 채널 목록
    * */
    @GetMapping("/list")
    public List<ChannelResponse> getChannels(Long groupId) {
        return channelService.getChannels(groupId);
    }
}
