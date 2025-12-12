package com.example.chat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /*곻통에러*/
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_001", "Invalid Input Value"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_002", "정보가 없습니다."),
    /*로그인에러*/
    INVALID_INPUT_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "LOGIN_001", "유효하지않는 아이디 또는 비밀번호 입니다."),
    /*회원에러*/
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "가입된 사용자가 아닙니다."),
    /*회원가입 에러*/
    DUPLICATED_USER(HttpStatus.BAD_REQUEST, "SIGNUP_001", "이미 가입된 회원입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
