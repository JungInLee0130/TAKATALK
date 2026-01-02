package com.example.chat.login.service;

import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.login.dto.UserLoginForm;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.UserDetailsServiceImpl;
import com.example.chat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final MailService mailService;
    private final TokenService tokenService;

    public void login(UserLoginForm userLoginForm) {
        SiteUser siteUser = userService.findByEmail(userLoginForm.getEmail());

        siteUser.isPasswordMatched(passwordEncoder, siteUser.getPassword());

        // 2. 인증 객체 생성 및 Context 저장
        // a. UserDetails 정보 가져오기
        UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginForm.getEmail());

        // b. Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void requestPasswordChange(String email) {
        SiteUser siteUser = userService.findByEmail(email);

        String token = tokenService.createAndSaveToken(email);

        mailService.sendChangePasswordMail(token, siteUser);
    }
}
