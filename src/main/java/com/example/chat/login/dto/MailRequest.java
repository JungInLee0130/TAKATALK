package com.example.chat.login.dto;

import jakarta.validation.constraints.Email;

public record MailRequest (@Email String mail){
}
