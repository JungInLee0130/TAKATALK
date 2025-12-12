package com.example.chat.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /*곻통에러*/
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_001", "INVALID INPUT VALUE"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_002", "정보가 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_003", "INTERNAL SERVER ERROR"),
    /*로그인에러*/
    INVALID_INPUT_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "LOGIN_001", "유효하지않는 아이디 또는 비밀번호 입니다."),
    /*비밀번호 찾기 에러*/
    RESET_TOKEN_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "RESET_TOKEN_001", "토큰이 존재하지않습니다."),
    RESET_TOKEN_INVALID_EXCEPTION(HttpStatus.BAD_REQUEST, "RESET_TOKEN_002", "토큰이 만료되었습니다."),
    /*회원에러*/
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "가입된 사용자가 아닙니다."),
    /*회원가입 에러*/
    DUPLICATED_USER(HttpStatus.BAD_REQUEST, "SIGNUP_001", "이미 가입된 회원입니다."),
    /*그룹 에러*/
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP_001", "해당 그룹이 없습니다."),
    /*카테고리 에러*/
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_001", "해당 카테고리가 없습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
