package com.example.chat.user.controller;

import com.example.chat.user.dto.ProfileRequest;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URI;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public String profilePage() {
        return "profile/profile";
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateProfile(@Valid ProfileRequest request,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        userService.updateProfile(userDetails.getId(), request);
        // 원래 있던 URI로 다시 돌아가는법 없나?
        return ResponseEntity.created(URI.create("/main")).build();
    }
}
