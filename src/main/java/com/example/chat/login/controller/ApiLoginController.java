package com.example.chat.login.controller;

import com.example.chat.login.dto.MailRequest;
import com.example.chat.login.dto.PasswordChangeRequest;
import com.example.chat.login.dto.UserLoginForm;
import com.example.chat.login.dto.VerificationResetTokenRequest;
import com.example.chat.login.service.LoginService;
import com.example.chat.login.service.TokenService;
import com.example.chat.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Login Api", description = "로그인과 관련된 Api, 회원가입 등 포함")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class ApiLoginController {
    private final UserService userService;
    private final TokenService tokenService;
    private final LoginService loginService;

    @Operation(summary = "비밀번호 변경 메일보내기", description = "비밀번호 재설정 메일을 보냅니다.")
    @PostMapping("/send-change-password")
    public ResponseEntity<Void> sendChangePasswordMail(@Valid @RequestBody MailRequest request) {
        loginService.requestPasswordChange(request.mail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @PostMapping("/change-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordChangeRequest request) {
        userService.resetPassword(request.password(), request.token());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리셋 토큰 유효성 검증", description = "리셋토큰이 유효한지 검증합니다.")
    @PostMapping("/verify-resetToken")
    public ResponseEntity<String> verifyResetToken(@Valid @RequestBody VerificationResetTokenRequest request) {
        String email = tokenService.validateTokenAndGetEmail(request.resetToken());
        return ResponseEntity.ok(email);
    }
}
