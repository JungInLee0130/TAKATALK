package com.example.chat.global.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class) // 통일성을 위해 return 형식 builder 패턴
public class ApiGlobalExceptionHandler {

    /*
     * javax.validation.Valid or @Validated로 binding error 발생시 발생한다.
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException : {}", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /*
     * @ModelAttribute로 binding error 발생시 BindingException 발생한다.
     * */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException : {}", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /* 파일 용량 초과 */
    /*@ExceptionHandler(FileSizeLimitExceededException.class)
    protected ResponseEntity<ErrorResponse> handleFileSizeLimitExceededException(FileSizeLimitExceededException e) {
        log.error("handleFileSizeLimitExceededException : {}", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, "FILE_SIZE_EXCEED");
    }*/

    /* 데이터 무결성 위반 */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("handleDataIntegrityViolationException : {}", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.CONFLICT);
        return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                .body(response);
    }

    /* 엔티티를 찾을수 없을때 */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException e) {
        log.error("handleNotFound : {}", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    /* Spring Security 권한 부족시 발생하는 예외 */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException : {}", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.ACCESS_DENIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    /* 사용자 정의 에러 */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException : {}", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getStatus())
                .body(response);
    }

    /*
     * 최상위 ExceptionHandler
     * */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception : ", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
