package com.example.chat.user.domain;

import jakarta.validation.constraints.Email;

public record UserFindRequest (@Email String mail){
}
