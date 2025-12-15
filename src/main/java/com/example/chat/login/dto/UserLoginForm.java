package com.example.chat.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginForm {
    @Email
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Size(min = 8, max = 25, message = "유효하지않는 아이디 또는 비밀번호 입니다.")
    private String password;

    @Builder
    public UserLoginForm(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
