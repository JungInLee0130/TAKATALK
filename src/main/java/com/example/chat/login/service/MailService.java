package com.example.chat.login.service;

import com.example.chat.exception.CustomException;
import com.example.chat.exception.ErrorCode;
import com.example.chat.login.domain.SignupRequest;
import com.example.chat.login.entity.MailUser;
import com.example.chat.login.repository.MailRepository;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final UserRepository userRepository;
    private final MailRepository mailRepository;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String SENDER_EMAIL;

    private static final Map<String, Integer> verificationCodes = new HashMap<>();

    /*
    * 인증코드 자동생성 메서드
    * */
    public static void createNumber(String mail){
        // 100000~999999 사이의 숫자생성
        int number = new Random().nextInt(900000) + 100000;
        verificationCodes.put(mail, number);
    }


    private void createFakeNumber(String mail) {
        int number = new Random().nextInt(900000) + 100000;
        log.info("verficationCodes : {}", number);
        verificationCodes.put(mail, number);
    }

    private void createFakeMail(String mail) {
        createFakeNumber(mail);
    }


    /*
     * 이메일 전송
     * */
    public MimeMessage createMail(String mail) {
        createNumber(mail);
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(SENDER_EMAIL);
            helper.setTo(mail);
            helper.setSubject("DISCORD 이메일 인증번호");
            String body = "<h2>DISCORD에 오신것을 환영합니다!</h2>" +
                    "<p>아래의 인증번호를 입력하세요.</p>" +
                    "<h1>" +
                    verificationCodes.get(mail) +
                    "</h1>" +
                    "<h3>감사합니다.</h3>";
            helper.setText(body, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }


    public CompletableFuture<Integer> sendMailFake(String mail) {
        createFakeMail(mail);
        log.info("Finish createFakeMail : {}");
        return CompletableFuture.completedFuture(verificationCodes.get(mail));
    }

    /*
    * createMail() 메서드의 내용을 이메일로 전송
    * */
    public CompletableFuture<Integer> sendMail(String mail) {
        MimeMessage message = createMail(mail);
        log.info("message : {}", message);
        javaMailSender.send(message);
        return CompletableFuture.completedFuture(verificationCodes.get(mail));
    }

    /*
     * 이메일 인증코드 검증
     * */
    public boolean verifyCode(String mail, int code) {
        Integer storedCode = verificationCodes.get(mail);
        return storedCode != null && storedCode == code;
    }
    
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

        SiteUser siteUser = userRepository.findByEmail(mail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        siteUser.setPassword(tempPassword);

        log.info("임시 비밀번호 저장완료 : {}", siteUser.getPassword());
        return tempPassword;
    }

    /*
    * 임시 비밀번호 검증
    * */
    public boolean verifyTemporaryPassword(String mail, String tempPassword) {
        MailUser mailUser = mailRepository.findByEmail(mail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return mailUser.getPassword().equals(tempPassword);
    }


    /*
    * 중복 메일 검사
    * */
    public boolean checkDuplicatedEmail(String mail) {
        Optional<MailUser> mailUser = mailRepository.findByEmail(mail);

        return mailUser.isPresent() ? true : false;
    }


    /*
    *
    * */
    public void sendTemporaryPasswordMailFake(String mail, String temporaryPassword) {
        log.info("임시 비밀번호 전송완료");
    }

    public boolean existsByUsername(String email) {
        return mailRepository.existsByEmail(email);
    }

    public MailUser signup(SignupRequest request) {
        MailUser mailUser = MailUser.builder()
                .email(request.email())
                .password(request.password())
                .build();
        mailRepository.save(mailUser);
        return mailUser;
    }
}
