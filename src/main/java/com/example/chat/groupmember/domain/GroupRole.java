package com.example.chat.groupmember.domain;

import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum GroupRole {
    ADMIN(3), OWNER(2), USER(1);

    private final int level;

    GroupRole(int level) {
        this.level = level;
    }
    // 멤버 권한 체크
    public boolean hasPermission(GroupRole requiredRole) {  // requiredRole : GROUPROLE.OWNER...
        return this.level >= requiredRole.level;    // ADMIN > OWNER > USER
    }
}
