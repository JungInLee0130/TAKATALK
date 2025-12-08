package com.example.chat;

import com.example.chat.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {
    @GetMapping("/index")
    public String index(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                        Model model) {
        model.addAttribute("username", customUserDetails.getUsername());
        return "index";
    }
}
