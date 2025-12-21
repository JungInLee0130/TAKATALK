package com.example.chat.user.dto;

import jakarta.validation.constraints.Email;

public record UserFindRequest (@Email String mail){
}
