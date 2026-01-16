package com.example.chat.channel.controller;

import com.example.chat.channel.dto.*;
import com.example.chat.channel.service.ChannelService;
import com.example.chat.channel.service.ChatMessageService;
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
@RequestMapping("/group/{groupId}/channel")
public class ChannelController {
    private final ChannelService channelService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    /*
     * 채널 생성
     * */
    @PostMapping(value = "/create", produces = "application/string;charset=UTF-8") // UTF-8 설정
    public ResponseEntity<Void> createChannel(@PathVariable(name = "groupId") Long groupId,
                                              @PathVariable(name = "categoryId", required = false) Long categoryId,
                                              @RequestBody CreateChannelRequest request){
        ChannelCreateResponse response = channelService.createChannel(groupId, categoryId, request);
        URI uri = URI.create("/group/access/" + response.getGroupId());
        return ResponseEntity.created(uri).build();
    }
    /*
    * 채널입장
    * */
    @GetMapping("/{channelId}")
    public String enterChannel(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable(name = "groupId") Long groupId,
                               @PathVariable(name = "channelId") Long channelId,
                               Model model) {
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
