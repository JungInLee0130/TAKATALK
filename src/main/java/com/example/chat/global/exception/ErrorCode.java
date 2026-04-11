package com.example.chat.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /*곻통에러*/
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "COMMON_001", "인풋값이 잘못되었습니다."), // 400
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "COMMON_002", "권한이 없습니다."), // 403
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "COMMON_003", "정보가 없습니다."), // 404
    CONFLICT(HttpStatus.CONFLICT.value(), "COMMON_004", "동시성, 무결성 문제입니다."), // 409 : 동시성, 무결성 문제
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "COMMON_005", "서버 내부 오류입니다."), // 500
    /*로그인에러*/
    INVALID_INPUT_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST.value(), "LOGIN_001", "유효하지않는 아이디 또는 비밀번호 입니다."),
    SESSION_INVALID_ERROR(HttpStatus.BAD_REQUEST.value(), "LOGIN_002", "로그인이 만료되었습니다. 다시 로그인해주세요."),
    /*비밀번호 찾기 에러*/
    RESET_TOKEN_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND.value(), "RESET_TOKEN_001", "토큰이 존재하지않습니다."),
    RESET_TOKEN_INVALID_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "RESET_TOKEN_002", "토큰이 만료되었습니다."),
    /*회원에러*/
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "MEMBER_001", "가입된 사용자가 아닙니다."),
    /*회원가입 에러*/
    DUPLICATED_USER(HttpStatus.BAD_REQUEST.value(), "SIGNUP_001", "이미 가입된 회원입니다."),
    /*그룹 에러*/
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "GROUP_001", "해당 그룹이 없습니다."),
    GROUP_PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), "GROUP_002", "권한이 없습니다."),
    ALREADY_JOINED_GROUP(HttpStatus.BAD_REQUEST.value(), "GROUP_003", "이미 가입한 그룹입니다."),
    INVALID_INVITE_CODE(HttpStatus.BAD_REQUEST.value(), "GROUP_004", "초대 코드가 유효하지않습니다"),    // 만료, 불일치
    /*카테고리 에러*/
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "CATEGORY_001", "해당 카테고리가 없습니다."),
    /*채널 에러*/
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "CHANNEL_001", "해당 채널이 없습니다."),
    /*그룹멤버 에러*/
    GROUP_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "GROUP_MEMBER_001", "해당 그룹멤버가 없습니다."),
    GROUP_MEMBER_PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), "GROUP_MEMBER_002", "권한이 없습니다."),
    /*메일 에러*/
    MAIL_CREATE_ERROR(HttpStatus.BAD_REQUEST.value(), "MAIL_001", "비밀번호 재설정 요청 메일 생성 실패 오류");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
