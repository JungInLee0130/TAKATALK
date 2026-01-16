package com.example.chat.group.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupJoinRequest {
    @NotNull
    private String inviteCode;

    public GroupJoinRequest(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
