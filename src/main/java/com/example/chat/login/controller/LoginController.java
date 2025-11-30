package com.example.chat.login.controller;

import com.example.chat.login.domain.MailRequest;
import com.example.chat.login.domain.UserLoginForm;
import com.example.chat.login.service.LoginService;
import com.example.chat.login.service.MailService;
import com.example.chat.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final UserService userService;

    private final MailService mailService;

    @GetMapping
    public String loginPage(UserLoginForm userLoginForm){
        return "login/login";
    }

    /*
    * 로그인
    * */
    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginForm userLoginForm) {
        log.info("email : {}, password : {}", userLoginForm.email(), userLoginForm.password());

        loginService.login(userLoginForm);
        log.info("LOGIN_SUCCESS");

        return ResponseEntity.ok("LOGIN_SUCCESS");
    }

    /*
    * 비밀번호 찾기 : 이메일 전송
    * */
    @PostMapping("/find-password")
    public ResponseEntity<String> findPassword(@Valid @RequestBody MailRequest mailRequest){
        if (userService.existsByEmail(mailRequest.mail())) {
            String temporaryPassword = mailService.createTemporaryPassword(mailRequest.mail());
            mailService.sendTemporaryPasswordMailFake(mailRequest.mail(), temporaryPassword);
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.ok("해당 이메일로 가입된 사용자가 없습니다.");
        }
    }
}
