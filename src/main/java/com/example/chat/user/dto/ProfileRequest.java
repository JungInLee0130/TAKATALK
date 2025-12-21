package com.example.chat.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileRequest {
    private String nickname;
    private MultipartFile profileImage; // 실제 파일 데이터
}
