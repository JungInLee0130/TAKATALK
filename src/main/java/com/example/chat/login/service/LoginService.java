package com.example.chat.login.service;

import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.login.dto.UserLoginForm;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserService userService;

    public void login(UserLoginForm userLoginForm) {
        SiteUser siteUser = userService.findByEmail(userLoginForm.email());
        if (!(siteUser.getPassword().equals(userLoginForm.password()))) {
            throw new CustomException(ErrorCode.INVALID_INPUT_EMAIL_OR_PASSWORD);
        }
    }
}
