package com.example.chat.visitor.controller;

import com.example.chat.visitor.service.VisitorsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/visitors")
public class VisitorsController {
    private final VisitorsService visitorsService;

    /*@GetMapping
    public String enterChannel(@RequestParam Long visitorsId, @RequestParam Long channelId) {
        visitorsService.enterChannel(visitorsId, channelId);
    }*/
}
