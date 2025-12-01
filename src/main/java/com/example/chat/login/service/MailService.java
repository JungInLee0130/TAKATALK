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
     * 랜덤 비밀번호 생성
     * */
    private static String generateRandomPassword() {

        int length = 8;
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append((char) (random.nextInt(10) + '0'));
        }

        log.info("random password : {}", sb.toString());
        return sb.toString();
    }

    /*
     * 임시 비밀번호 메일전송
     * */
    public void sendTemporaryPasswordMail(String mail, String tempPassword) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(SENDER_EMAIL);
            helper.setTo(mail);
            helper.setSubject("DISCORD : 임시 비밀번호");
            String body = "<h2>DISCORD에 오신것을 환영합니다!</h2>" +
                    "<p>아래의 임시 비밀번호를 사용하세요.</p>" +
                    "<h1>" +
                    tempPassword +
                    "</h1>" +
                    "<h3>반드시 비밀번호를 재설정하세요.</h3>";
            helper.setText(body, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("임시 비밀번호 전송 오류", e);
        }
    }

    /*
    * 임시 비밀번호 생성 및 DB 업데이트
    * */
    @Transactional
    public String createTemporaryPassword(String mail) {
        String tempPassword = generateRandomPassword();

        SiteUser siteUser = userService.findByEmail(mail);

        siteUser.setPassword(tempPassword);

        log.info("임시 비밀번호 저장완료 : {}", siteUser.getPassword());
        return tempPassword;
    }

    /*
    *
    * */
    public void sendTemporaryPasswordMailFake(String mail, String temporaryPassword) {
        log.info("임시 비밀번호 전송완료");
    }

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
