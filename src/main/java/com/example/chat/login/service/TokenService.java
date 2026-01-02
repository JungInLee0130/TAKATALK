package com.example.chat.login.service;

import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.login.entity.ResetToken;
import com.example.chat.login.repository.ResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        ResetToken resetToken = ResetToken.create(mail);
        resetTokenRepository.save(resetToken);
        return resetToken.getUuid();
    }

    /*
    * 토큰 검증 및 이메일 조회
    * */
    public String validateTokenAndGetEmail(String uuid) {
        // 1. 토큰 없음
        ResetToken resetToken = resetTokenRepository.findByUuid(uuid)
                .orElseThrow(() -> new CustomException(ErrorCode.RESET_TOKEN_NOT_FOUND_EXCEPTION));

        // 2. 토큰 만료
        if (resetToken.isExpired()) {
            throw new CustomException(ErrorCode.RESET_TOKEN_INVALID_EXCEPTION);
        }

        // 정상
        return resetToken.getEmail();
    }

    /*
    * 토큰 삭제
    * */
    public void deleteToken(String token) {
        ResetToken resetToken = resetTokenRepository.findByUuid(token)
                .orElseThrow(() -> new CustomException(ErrorCode.RESET_TOKEN_NOT_FOUND_EXCEPTION));

        resetTokenRepository.delete(resetToken);
    }


}
