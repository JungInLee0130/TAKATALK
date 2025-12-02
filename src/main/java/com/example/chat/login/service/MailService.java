package com.example.chat.login.service;

import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import com.example.chat.user.service.UserService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final TokenService tokenService;

    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String SENDER_EMAIL;

    @Value("${BASE_URL}")
    private String baseUrl;

    /*
    * 비밀번호 재설정메일 전송
    * */
    public void sendChangePasswordMail(SiteUser siteUser) {
        // 1. OTP 토큰 생성 및 DB 저장
        String resetToken = tokenService.createAndSaveToken(siteUser.getEmail());

        log.info("resetToken 생성완료(JSON) : {}", resetToken);

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(siteUser.getEmail());
            helper.setSubject("Discord 비밀번호 재설정 요청");
            helper.setText(setContext(resetToken, siteUser), true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 재설정 요청 메일 전송 오류", e);
        }
    }

    private String setContext(String resetToken, SiteUser siteUser) {
        Context context = new Context();
        context.setVariable("username", siteUser.getUsername());
        context.setVariable("resetToken", resetToken);
        context.setVariable("baseUrl", baseUrl);
        String htmlContent = templateEngine.process("/login/change-password-mail-template", context);
        return htmlContent;
    }
}
