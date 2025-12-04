package com.example.chat.login.service;

import com.example.chat.global.aop.annotation.Timer;
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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<String> sendChangePasswordMail(SiteUser siteUser) {
        MimeMessage message = createChangePasswordMail(siteUser);
        javaMailSender.send(message);
        return CompletableFuture.completedFuture("SUCCESS");
    }



    /*
     * 비밀번호 재설정메일 생성
     * */
    private MimeMessage createChangePasswordMail(SiteUser siteUser) {
        String resetToken = tokenService.createAndSaveToken(siteUser.getEmail());

        log.info("resetToken 생성완료(JSON) : {}", resetToken);

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(siteUser.getEmail());
            helper.setSubject("Discord 비밀번호 재설정 요청");
            helper.setText(setContext(resetToken, siteUser), true);
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 재설정 요청 메일 전송 오류", e);
        }

        return message;
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
