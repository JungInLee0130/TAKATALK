package com.example.chat.user.service;

import com.example.chat.global.file.FileService;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.login.service.TokenService;
import com.example.chat.user.dto.ProfileRequest;
import com.example.chat.user.domain.UserCreateForm;
import com.example.chat.user.dto.UserResponse;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Transactional
    public void updateProfile(Long siteUserId, ProfileRequest request) throws IOException {
        SiteUser siteUser = userRepository.findById(siteUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 1. 닉네임을 입력했다면 저장
        if (StringUtils.hasText(request.getNickname())) {
            siteUser.updateNickname(request.getNickname());
        }

        // 2. 프로필 이미지가 null이 아니라면 저장
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            // FileService로 분리
            String savedFileName = fileService.storeFile(request.getProfileImage(), siteUser.getProfile());
            siteUser.updateProfile(savedFileName);
        }
    }

    @Transactional
    public void deleteProfile(Long siteUserId){
        SiteUser siteUser = findById(siteUserId);

        // 1. 기본 이미지 아닐때만 삭제
        if (StringUtils.hasText(siteUser.getProfile())) {
            fileService.deleteProfile(siteUser.getProfile());

            // 2. DB 값을 Null로 변경
            siteUser.updateProfile(null);
        }
    }

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
                .profile(null)
                .build();

        return siteUser;
    }

    public SiteUser getReferenceById(Long siteUserId) {
        return userRepository.getReferenceById(siteUserId); // 프록시 객체 조회. 더 빠른 탐색가능. Optional이 아니기때문에 실제로 존재할때만사용.
    }

    public SiteUser findById(Long siteUserId) {
        return userRepository.findById(siteUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));    // 진짜 객체 조회. Optional 적용.
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
        siteUser.updatePassword(password);
    }

    @Transactional
    public void changePassword(String email, String newPassword, String token) {
        // 비밀번호 변경
        changePassword(email, newPassword);
        // 토큰은 1회용이므로 삭제
        tokenService.deleteToken(token);
    }

    public UserResponse getUserDetails(Long siteUserId) {
        SiteUser siteUser = userRepository.findById(siteUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        UserResponse response = UserResponse.builder()
                .nickname(siteUser.getNickname())
                .profile(siteUser.getProfile())
                .build();

        log.info("profile : {}", siteUser.getProfile());

        return response;
    }
}

