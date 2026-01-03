package com.example.chat.login.controller;

import com.example.chat.login.dto.MailRequest;
import com.example.chat.login.dto.PasswordChangeRequest;
import com.example.chat.login.dto.UserLoginForm;
import com.example.chat.login.dto.VerificationResetTokenRequest;
import com.example.chat.login.service.LoginService;
import com.example.chat.login.service.MailService;
import com.example.chat.login.service.TokenService;
import com.example.chat.user.service.UserService;
import jakarta.servlet.http.HttpSession;
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
    private final UserService userService;
    private final TokenService tokenService;
    private final LoginService loginService;

    /*
    * 로그인 페이지
    * */
    @GetMapping
    public String loginPage(Model model,
                            HttpSession session){
        String errorMsg = (String) session.getAttribute("loginErrorMsg");

        UserLoginForm userLoginForm = new UserLoginForm();

        if (errorMsg != null) {
            userLoginForm.setEmail((String) session.getAttribute("email"));

            model.addAttribute("error", "true");
            model.addAttribute("exception", errorMsg);

            session.removeAttribute("loginErrorMsg");
            session.removeAttribute("email");
        }

        model.addAttribute("userLoginForm", userLoginForm);

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
    public String changePasswordPage(@RequestParam(name = "token") String token,
                                     Model model) {
        String email = tokenService.validateTokenAndGetEmail(token);

        if (email != null) {
            model.addAttribute("token", token);
            return "login/change-password";
        }

        return "redirect:/login/token-expired";
    }

    /*
     * 비밀번호 변경 메일보내기(비동기처리)
     * */
    /*@PostMapping("/send-change-password")
    public CompletableFuture<ResponseEntity<String>> sendChangePasswordMail(@Valid @RequestBody MailRequest request) {
        loginService.requestPasswordChange(request.mail());

        return mailService.sendChangePasswordMail(siteUser)
                .thenApply(str -> ResponseEntity.ok(str));
    }*/

    @PostMapping("/send-change-password")
    public ResponseEntity<String> sendChangePasswordMail(@Valid @RequestBody MailRequest request) {
        loginService.requestPasswordChange(request.mail());
        return ResponseEntity.ok().build();
    }
    
    /*
    * 비밀번호 변경
    * */
    @PostMapping("/change-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordChangeRequest request) {
        userService.resetPassword(request.password(), request.token());

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
