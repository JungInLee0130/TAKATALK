package com.example.chat.login.controller;

import com.example.chat.login.domain.MailRequest;
import com.example.chat.login.service.LoginService;
import com.example.chat.login.domain.UserLoginForm;
import com.example.chat.login.service.MailService;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;

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
        //model.addAttribute("userLoginForm", "");
        return "login/login";
    }

    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginForm userLoginForm) {
        log.info("email : {}, password : {}", userLoginForm.email(), userLoginForm.password());
        /*폼 입력 에러*/
        /*
         * 비밀번호 유효성검사 : 유효하지않는 아이디 또는 비밀번호 입니다.
         * */
        /*if (!(userLoginForm.password().length() == 0 || userLoginForm.password() == null)) {
            if (8 > userLoginForm.password().length() || userLoginForm.password().length() > 25) {
                bindingResult.rejectValue("email", "INVALID_INPUT_VALUE" ,"유효하지않는 아이디 또는 비밀번호 입니다.");
                model.addAttribute("userLoginForm", userLoginForm);
                return "login/login";
            }
        }*/

        /*if (bindingResult.hasErrors()) {
            model.addAttribute("userLoginForm", userLoginForm);
            return "login/login";
        }*/

        //SiteUser siteUser = userService.findByEmail(userLoginForm.email());

        /*회원이 존재하지 않으면*/
        /*if (siteUser == null) {
            log.info("로그인 실패 : 회원 정보 없음.");
            bindingResult.addError(new ObjectError("userLoginForm", "이메일 또는 비밀번호를 잘못 입력했습니다. 입력하신 내용을 다시 확인해주세요."));
            model.addAttribute("userLoginForm", userLoginForm);
            return "login/login";
        }*/

        loginService.login(userLoginForm);
        log.info("LOGIN_SUCCESS");

        //return "index";
        return ResponseEntity.ok("LOGIN_SUCCESS");
    }

    /*비밀번호 찾기 : 이메일 전송*/
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
