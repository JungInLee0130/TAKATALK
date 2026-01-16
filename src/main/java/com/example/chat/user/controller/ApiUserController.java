package com.example.chat.user.controller;

import com.example.chat.user.dto.UserProfileResponse;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "User Api", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ApiUserController {
    private final UserService userService;

    @Operation(summary = "로그인 체크 테스트", description = "현재 API 서버가 작동하는지 확인합니다.")
    @GetMapping("/test")
    public Map<String, String> test() {
        return Map.of("message", "TAKATALK API 서버가 정상적으로 작동합니다.");
    }

    @Operation(summary = "사용자 프로필 정보 조회", description = "사용자 프로필 정보를 조회합니다.")
    @GetMapping("/profile") // profile 컨트롤러에 있는것 user로 통합
    public ResponseEntity<UserProfileResponse> getUserProfileInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse response = userService.getSiteUserProfileInfo(userDetails.getId());
        return ResponseEntity.ok(response);
    }
}
