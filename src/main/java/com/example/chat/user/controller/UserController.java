package com.example.chat.user.controller;

import com.example.chat.global.exception.ErrorCode;
import com.example.chat.user.domain.UserCreateForm;
import com.example.chat.user.dto.UserProfileResponse;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    /*
    * 회원 가입 페이지 접속
    * */
    @GetMapping("/signup")
    public String signupPage(UserCreateForm userCreateForm) {
        return "signup/signup";
    }

    /*
    * 회원 가입 신청
    * */
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute UserCreateForm userCreateForm
            , BindingResult bindingResult
            , Model model) {
        /*에러처리*/
        /*
        * 닉네임이 있다면 : 3~25자 사이여야한다.
        * */
        //SiteUser.validateNicknameIfExist(userCreateForm.nickname());
        if (StringUtils.hasText(userCreateForm.nickname())) {
            if (3 > userCreateForm.nickname().length() || userCreateForm.nickname().length() > 25 ) {
                bindingResult.rejectValue("nickname", "INVALID_NICKNAME_SIZE", "닉네임은 3~25자여야합니다.");
                return "signup/signup";
            }
        }

        /*
        * 생년월일 에러처리
        * */
        if(!validateBirthDay(userCreateForm)) {
            bindingResult.reject("NOT_INVALID_BIRTHDAY_VALUE", "유효한 생년월일을 입력해주세요.");
            return "signup/signup";
        }

        /*글로벌 에러 처리*/
        /*
        * 이메일이 존재하지않는다면
        * */
        if (userService.existsByEmail(userCreateForm.email())) {
            bindingResult.rejectValue("email", ErrorCode.DUPLICATED_USER.getCode(), ErrorCode.DUPLICATED_USER.getMessage());
            model.addAttribute("userCreateForm", userCreateForm);
            return "signup/signup";
        }

        /*모든 에러 처리*/
        if (bindingResult.hasErrors()) {
            /*회원가입 실패시 입력 데이터값 유지*/
            model.addAttribute("userCreateForm", userCreateForm);
            return "signup/signup";
        }

        userService.create(userCreateForm);

        /*로그인 화면으로 이동*/
        return "redirect:/login";
    }

    private boolean validateBirthDay(UserCreateForm userCreateForm) {
        /*생년월일은 필수값*/
        if (!(StringUtils.hasText(userCreateForm.birthYear())
                && StringUtils.hasText(userCreateForm.birthMonth())
                && StringUtils.hasText(userCreateForm.birthDay()))) {
            return false;
        }

        int birthYear = Integer.parseInt(userCreateForm.birthYear());
        int birthMonth = Integer.parseInt(userCreateForm.birthMonth());
        int birthDay = Integer.parseInt(userCreateForm.birthDay());

        boolean isValidBirthDate = true;

        switch (birthMonth) {
            case 1:case 3:case 5:case 7:case 8:case 10:case 12:
                break;
            case 2:
                if (birthYear % 400 == 0
                        || birthYear % 4 == 0 && birthYear % 100 != 0) {
                    if (birthDay >= 30) {
                        isValidBirthDate = false;
                    }
                } else {
                    // 28일까지
                    if (birthDay >= 29) {
                        isValidBirthDate = false;
                    }
                }
                break;
            case 4:case 6:case 9:case 11:
                if (birthDay == 31) {
                    isValidBirthDate = false;
                }
                break;
        }

        if (!isValidBirthDate) {
            return false;
        }
        return true;
    }

    /*@PostMapping("/find-user")
    public ResponseEntity<String> findUser(@Valid @RequestBody UserFindRequest request) {
        log.info("find-user");
        boolean isExistsEmail = userService.existsByEmail(request.mail());

        return ResponseEntity.ok("success");
    }*/

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getSiteUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse response = userService.getSiteUserProfileInfo(userDetails.getId());
        return ResponseEntity.ok(response);
    }
}
