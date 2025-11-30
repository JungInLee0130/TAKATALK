package com.example.chat.user.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record UserFindRequest (@Email @NotEmpty String mail){
}
