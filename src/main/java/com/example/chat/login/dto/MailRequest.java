package com.example.chat.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record MailRequest (@Email @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.") String mail){
}
