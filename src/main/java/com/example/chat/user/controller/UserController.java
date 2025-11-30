package com.example.chat.user.controller;

import com.example.chat.user.domain.UserCreateForm;
import com.example.chat.user.domain.UserFindRequest;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute UserCreateForm userCreateForm
            , BindingResult bindingResult
            , Model model) {
        /*에러처리*/
        /*닉네임이 있다면 : 3~25자 사이여야한다.*/
        if (StringUtils.hasText(userCreateForm.nickname())) {
            if (3 > userCreateForm.nickname().length() || userCreateForm.nickname().length() > 25 ) {
                bindingResult.rejectValue("nickname", "INVALID_NICKNAME_SIZE", "닉네임은 3~25자여야합니다.");
                //return "redirect:/user/signup";
            }
        }
        /*생년월일은 필수값*/
        if (!(StringUtils.hasText(userCreateForm.birthYear()) && StringUtils.hasText(userCreateForm.birthMonth())
                && StringUtils.hasText(userCreateForm.birthDay()))) {
            bindingResult.rejectValue("birthYear", "EMPTY_BIRTHDAY", "생년월일은 필수 값 입니다.");
            //return "redirect:/user/signup";
        }

        /*글로벌 에러 처리*/

        /*모든 에러 처리*/
        if (bindingResult.hasErrors()) {

            log.info("회원가입 실패, UserCreateForm2 : {}", userCreateForm);

            bindingResult.getFieldErrors().forEach(error -> {
                log.warn("Field : {}, Code : {}, Message : {}",
                        error.getField(),
                        error.getCode(),
                        error.getDefaultMessage());
            });

            /*회원가입 실패시 입력 데이터값 유지*/
            model.addAttribute("userCreateForm2", userCreateForm);

            /*회원가입 페이지로 다시 리턴*/
            return "signup/signup";
        }

        SiteUser siteUser = userService.create(userCreateForm);

        log.info("회원가입 성공, SITEUSER: {}", siteUser.getId());

        /*로그인 화면으로 이동*/
        return "redirect:/login";
    }

    @PostMapping("/find-user")
    public ResponseEntity<String> findUser(@Valid @RequestBody UserFindRequest request) {
        log.info("find-user");
        boolean isExistsEmail = userService.existsByEmail(request.mail());

        return ResponseEntity.ok("success");
    }
}
