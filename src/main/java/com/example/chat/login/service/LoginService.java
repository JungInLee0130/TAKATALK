package com.example.chat.login.service;

import com.example.chat.global.security.jwt.JwtProvider;
import com.example.chat.login.dto.UserLoginForm;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MailService mailService;
    private final TokenService tokenService;

    @Transactional
    public String login(UserLoginForm userLoginForm) {
        SiteUser siteUser = userService.findByEmail(userLoginForm.getEmail());
        siteUser.isPasswordMatched(passwordEncoder, userLoginForm.getPassword());
        return jwtProvider.generateToken(siteUser.getEmail());
    }

    @Transactional
    public void requestPasswordChange(String email) {
        SiteUser siteUser = userService.findByEmail(email);
        String token = tokenService.createAndSaveToken(email);
        mailService.sendChangePasswordMail(token, siteUser);
    }


}
