package com.example.chat.user.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateForm(
        @Email
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        String email,
        String nickname,
        @Size(min = 3, max = 25)
        String username,
        @Size(min = 8, max = 25) String password,
        String birthYear,
        String birthMonth,
        String birthDay){
}
