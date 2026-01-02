package com.example.chat.login.service;

import com.example.chat.global.aop.annotation.Timer;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import com.example.chat.user.service.UserService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    @Value("${BASE_URL}")
    private String baseUrl;

    /*
     * 비밀번호 재설정 메일 전송
     * */
    /*@Timer
    public CompletableFuture<String> sendChangePasswordMail(SiteUser siteUser) {
        MimeMessage message = createChangePasswordMail(siteUser);
        javaMailSender.send(message);
        return CompletableFuture.completedFuture("SUCCESS");
    }*/

    @Timer
    @Async
    public void sendChangePasswordMail(String resetToken, SiteUser siteUser) {
        MimeMessage message = createChangePasswordMail(resetToken, siteUser);
        javaMailSender.send(message);
        log.info("메일 전송 완료 : {}", siteUser.getEmail());
    }



    /*
     * 비밀번호 재설정 메일 생성
     * */
    private MimeMessage createChangePasswordMail(String resetToken, SiteUser siteUser) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(siteUser.getEmail());
            helper.setSubject("TAKATALK 비밀번호 재설정 요청");
            helper.setText(setContext(resetToken, siteUser), true);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MAIL_CREATE_ERROR);
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
