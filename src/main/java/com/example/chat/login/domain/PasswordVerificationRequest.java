package com.example.chat.login.domain;

public record PasswordVerificationRequest (String mail,
                                           String tempPassword){
}
