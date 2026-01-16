package com.example.chat.groupmember.domain.annotation;

import com.example.chat.groupmember.domain.GroupRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireGroupRole {
    GroupRole value() default GroupRole.OWNER; // 기본값을 OWNER로 지정
}
