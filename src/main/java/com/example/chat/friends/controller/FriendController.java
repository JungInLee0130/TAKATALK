package com.example.chat.friends.controller;

import com.example.chat.friends.FriendService;
import com.example.chat.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    @GetMapping("/list")
    public String getFriendsList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                Model model) {
        model.addAttribute("friendsResponse", friendService.getFriendsResponses(userDetails.getId()));
        return "";
    }
}
