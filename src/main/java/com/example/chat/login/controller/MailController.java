package com.example.chat.login.controller;

import com.example.chat.login.domain.*;
import com.example.chat.login.entity.MailUser;
import com.example.chat.login.service.MailService;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {
    private final MailService mailService;
    private final UserService userService;


    @GetMapping
    public String mailPage(SignupRequest signupRequest) {
        return "fake-sign-up";
    }

    @GetMapping("/find-password")
    public String findPasswordPage() {
        return "fake-find-password";
    }

    /*
    * 회원가입
    * */
    @PostMapping("/signup")
    public String signup(@ModelAttribute("signupRequest") SignupRequest request) {
        log.info("email : {}, password : {}", request.email(), request.password());
        MailUser mailUser = mailService.signup(request);
        log.info("회원가입 완료 : [username : {}, password : {}]", request.email(), request.password());
        return "fake-find-password";
    }

    /*
     * 이메일 중복확인
     * */
    @PostMapping("/check-mail")
    public ResponseEntity<Boolean> checkDuplicatedEmail(@RequestBody MailRequest mailRequest) {
        log.info("check-mail");
        boolean isDuplicated = mailService.checkDuplicatedEmail(mailRequest.mail());
        return ResponseEntity.ok(isDuplicated);
    }

    /*
     * 인증번호 발송
     * */
    @PostMapping
    @ResponseBody
    public CompletableFuture<String> mailSend(@RequestBody MailRequest mailRequest) {
        log.info("mailsendFake");
        return mailService.sendMailFake(mailRequest.mail())
                .thenApply(number -> String.valueOf(number));
        /*return mailService.sendMail(mailRequest.mail())
                .thenApply(number -> String.valueOf(number));*/
    }

    /*
    * 인증번호 확인
    * */
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody MailVerificationRequest verificationRequest) {
        boolean isVerified = mailService.verifyCode(verificationRequest.mail(),
                verificationRequest.code());
        log.info("isVerified : {}", isVerified);
        return ResponseEntity.ok(isVerified ? "verified" : "verification failed");
    }

    /*
    * 임시 비밀번호 발송
    * */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody MailRequest mailRequest) {
        String email = mailRequest.mail();
        if (mailService.existsByUsername(email)) {
            String temporaryPassword = mailService.createTemporaryPassword(mailRequest.mail());
            mailService.sendTemporaryPasswordMailFake(mailRequest.mail(), temporaryPassword);
            return ResponseEntity.ok("임시 비밀번호가 이메일로 발송되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 이메일로 가입된 사용자가 없습니다.");
        }
    }

    /*
    * 임시 비밀번호 검증
    * */
    @PostMapping("/verify-temporary-password")
    public ResponseEntity<String> verifyTemporaryPassword(@RequestBody PasswordVerificationRequest request) {
        log.info("email : {}, password : {}", request.mail(), request.tempPassword());
        boolean isVerified = mailService.verifyTemporaryPassword(request.mail(), request.tempPassword());
        return isVerified ? ResponseEntity.ok("Verified") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed");
    }
}
