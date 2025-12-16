package com.example.chat.user.service;

import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.login.service.TokenService;
import com.example.chat.user.domain.UserCreateForm;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    public SiteUser create (UserCreateForm userCreateForm){
        /*중복 회원 체크*/
        userRepository.findByEmail(userCreateForm.email())
                .ifPresent(error -> {
                    throw new CustomException(ErrorCode.DUPLICATED_USER);
                });

        SiteUser siteUser = createUser(userCreateForm);
        userRepository.save(siteUser);

        return siteUser;
    }

    private SiteUser createUser(UserCreateForm userCreateForm) {
        String nickname = userCreateForm.nickname();
        /*닉네임이 없다면*/
        if (!StringUtils.hasText(userCreateForm.nickname())) {
            nickname = userCreateForm.username();
        }

        int year = Integer.parseInt(userCreateForm.birthYear());
        int month = Integer.parseInt(userCreateForm.birthMonth());
        int day = Integer.parseInt(userCreateForm.birthDay());

        LocalDate dateTime = LocalDate.of(year, month, day);

        SiteUser siteUser = SiteUser.builder()
                .username(userCreateForm.username())
                .email(userCreateForm.email())
                .nickname(nickname)
                .password(passwordEncoder.encode(userCreateForm.password()))
                .birthday(dateTime)
                .build();

        siteUser.setProfile("/images/meeng.png");

        return siteUser;
    }

    public SiteUser findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    public void changePassword(String email, String password) {
        SiteUser siteUser = findByEmail(email);
        siteUser.setPassword(password);
    }

    @Transactional
    public void changePassword(String email, String newPassword, String token) {
        /*트랜잭션으로 묶어야할듯*/
        // 비밀번호 변경
        changePassword(email, newPassword);
        // 토큰은 1회용.
        tokenService.deleteToken(token);
        /**/
    }
}

