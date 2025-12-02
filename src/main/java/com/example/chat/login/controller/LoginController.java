package com.example.chat.login.controller;

import com.example.chat.exception.CustomException;
import com.example.chat.exception.ErrorCode;
import com.example.chat.login.dto.MailRequest;
import com.example.chat.login.dto.PasswordChangeRequest;
import com.example.chat.login.dto.UserLoginForm;
import com.example.chat.login.dto.VerificationResetTokenRequest;
import com.example.chat.login.service.LoginService;
import com.example.chat.login.service.MailService;
import com.example.chat.login.service.TokenService;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final UserService userService;

    private final MailService mailService;
    private final TokenService tokenService;

    /*
    * 로그인 페이지
    * */
    @GetMapping
    public String loginPage(UserLoginForm userLoginForm){
        return "login/login";
    }

    /*
    * 비밀번호 변경 페이지 접속 메일
    * */
    @GetMapping("/change-password-mail-template")
    public String changePasswordTemplagePage (){
        return "login/change-password-mail-template";
    }
    
    /*
    * 비밀번호 변경 페이지 이동
    * */
    @GetMapping("/change-password")
    public String changePasswordPage(@RequestParam("token") String token,
                                     Model model) {
        String email = tokenService.validateTokenAndGetEmail(token);

        if (email != null) {
            model.addAttribute("token", token);
            return "login/change-password";
        }

        return "redirect:/login/token-expired";
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
     * 비밀번호 변경 메일보내기(비동기처리)
     * */
    @PostMapping("/send-change-password")
    public CompletableFuture<ResponseEntity<String>> sendChangePasswordMail(@Valid @RequestBody MailRequest request) {
        SiteUser siteUser = userService.findByEmail(request.mail());
        return mailService.sendChangePasswordMail(siteUser)
                .thenApply(str -> ResponseEntity.ok(str));
    }
    
    /*
    * 비밀번호 변경
    * */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        String token = request.token();
        String newPassword = request.password();

        String email = tokenService.validateTokenAndGetEmail(token);

        userService.changePassword(email, newPassword, token);

        return ResponseEntity.ok("SUCCESS");
    }

    /*
     * 검증 : resetToken
     * */
    @PostMapping("/verify-resetToken")
    public ResponseEntity<String> verifyResetToken(@Valid @RequestBody VerificationResetTokenRequest request) {
        String email = tokenService.validateTokenAndGetEmail(request.resetToken());
        return ResponseEntity.ok(email);
    }
}
