package com.example.chat.user.service;

import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.login.service.TokenService;
import com.example.chat.user.domain.UserCreateForm;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public SiteUser create (UserCreateForm userCreateForm){
        /*중복 회원 체크*/
        userRepository.findByEmail(userCreateForm.email())
                .ifPresent(error -> {
                    throw new CustomException(ErrorCode.DUPLICATED_USER);
                });

        SiteUser siteUser = SiteUser.createUser(userCreateForm);
        userRepository.save(siteUser);

        return siteUser;
    }

    public SiteUser findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public boolean existsByEmail(String email) {
        log.info("existsByEmail");
        return userRepository.existsByEmail(email);
    }


    public void changePassword(String email, String password) {
        log.info("chanagePassword : password : {}", password);
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

