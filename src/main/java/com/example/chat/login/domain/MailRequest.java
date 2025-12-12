package com.example.chat.login.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record MailRequest (@NotEmpty @Email String mail){
}
