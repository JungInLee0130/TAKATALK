package com.example.chat.login.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest (@NotEmpty String token,
                                     @Size(min = 8, max = 25, message = "유효하지않는 아이디 또는 비밀번호 입니다.") String password){
}
