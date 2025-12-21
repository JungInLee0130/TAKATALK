package com.example.chat.group.controller;

import com.example.chat.group.dto.GroupGetResponse;
import com.example.chat.group.service.GroupService;
import com.example.chat.group.dto.createGroupRequest;
import com.example.chat.user.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    /*
     * 그룹 리스트 조회
     * */
    @GetMapping("/list")
    public ResponseEntity<List<GroupGetResponse>> getGroupList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               Model model) {
        List<GroupGetResponse> groups = groupService.findAll(userDetails.getId());
        return ResponseEntity.ok(groups);
    }

    /*
    * 그룹 생성
    * */
    @PostMapping("/create")
    public ResponseEntity<Void> createGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @Valid createGroupRequest request) throws IOException {
        Long newGroupId = groupService.createGroup(userDetails, request);
        return ResponseEntity.created(URI.create("/group/access/" + newGroupId)).build();
    }
}
