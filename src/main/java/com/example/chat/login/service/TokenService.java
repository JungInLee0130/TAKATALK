package com.example.chat.login.service;

import com.example.chat.exception.CustomException;
import com.example.chat.exception.ErrorCode;
import com.example.chat.login.entity.ResetToken;
import com.example.chat.login.repository.ResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final ResetTokenRepository resetTokenRepository;

    /*
    * 토큰 생성 및 저장
    * */
    @Transactional
    public String createAndSaveToken(String mail) {
        // 토큰 생성 + 만료시간 -> h2에 저장
        ResetToken resetToken = createToken(mail);
        resetTokenRepository.save(resetToken);
        return resetToken.getUUID();
    }

    private ResetToken createToken(String mail) {
        // 15분
        long RESET_TOKEN_VALIDATION_MINUTE = 15L;
        LocalDateTime now = LocalDateTime.now();
        ResetToken resetToken = ResetToken.builder()
                .UUID(UUID.randomUUID().toString())
                .email(mail)
                .createdAt(now)
                .expiresAt(now.plusMinutes(15L))
                .build();
        return resetToken;
    }

    /*
    * 토큰 검증 및 이메일 조회
    * */
    public String validateTokenAndGetEmail(String UUID) {
        // 1. 토큰 없음
        ResetToken resetToken = resetTokenRepository.findByUUID(UUID)
                .orElseThrow(() -> new CustomException(ErrorCode.RESET_TOKEN_NOT_FOUND_EXCEPTION));

        // 2. 토큰 만료
        if (LocalDateTime.now().isAfter(resetToken.getExpiresAt())) {
            throw new CustomException(ErrorCode.RESET_TOKEN_INVALID_EXCEPTION);
        }

        // 정상
        return resetToken.getEmail();
    }

    /*
    * 토큰 삭제
    * */
    public void deleteToken(String token) {
        ResetToken resetToken = resetTokenRepository.findByUUID(token)
                .orElseThrow(() -> new CustomException(ErrorCode.RESET_TOKEN_NOT_FOUND_EXCEPTION));

        resetTokenRepository.delete(resetToken);
    }


}
