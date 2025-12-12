package com.example.chat.user.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateForm(@NotBlank @Email String email
        , String nickname
        , @Size(min = 3, max = 25) String username
        , @Size(min = 8, max = 25) String password
        , String birthYear
        , String birthMonth
        , String birthDay){
}
