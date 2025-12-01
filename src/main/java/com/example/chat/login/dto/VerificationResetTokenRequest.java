package com.example.chat.login.dto;

import jakarta.validation.constraints.NotEmpty;

public record VerificationResetTokenRequest (@NotEmpty String resetToken){
}
