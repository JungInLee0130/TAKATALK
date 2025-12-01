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

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final UserService userService;

    private final MailService mailService;
    private final TokenService tokenService;

    @GetMapping
    public String loginPage(UserLoginForm userLoginForm){
        return "login/login";
    }

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
        // requestparam을 받아서 사용하려면 model에 넣어줘야함.
        // 1. 이메일 검증
        String email = tokenService.validateTokenAndGetEmail(token);

        // 1-2. 이메일 검증완료
        if (email != null) {
            model.addAttribute("token", token);
            return "login/change-password";
        }

        // 1-1. 이메일 검증 실패
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
    * 비밀번호 찾기 : 이메일 전송
    * */
    @PostMapping("/find-password")
    public ResponseEntity<String> findPassword(@Valid @RequestBody MailRequest mailRequest){
        if (userService.existsByEmail(mailRequest.mail())) {
            String temporaryPassword = mailService.createTemporaryPassword(mailRequest.mail());
            mailService.sendTemporaryPasswordMail(mailRequest.mail(), temporaryPassword);
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.ok("해당 이메일로 가입된 사용자가 없습니다.");
        }
    }

    /*
     * 비밀번호 변경 메일보내기
     * */
    @PostMapping("/send-change-password")
    public ResponseEntity<String> sendChangePasswordMail(@Valid @RequestBody MailRequest request) {
        SiteUser siteUser = userService.findByEmail(request.mail());
        mailService.sendChangePasswordMail(siteUser);
        return ResponseEntity.ok("SUCCESS");
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
