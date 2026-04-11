package com.example.chat.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.security.Principal;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class StompGlobalExceptionHandler {
    private final SimpMessagingTemplate messagingTemplate;

    /* @Valid 검증 실패시 */
    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationException(MethodArgumentNotValidException e,
                                          Principal principal) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("Stomp 검증 실패 : {}", e);
        // 에러낸 사람에게만 보냄(구독자 한정)
        if (principal != null) {
            messagingTemplate.convertAndSendToUser(
                    principal.getName(),
                    "/sub/errors",
                    ErrorResponse.of("VALIDATION_ERROR", errorMessage)
            );
        }
    }

    @MessageExceptionHandler(CustomException.class)
    public void handleCustomException(CustomException e, Principal principal) {
        log.error("STOMP 비즈니스 로직 에러 : {}", e);

        if (principal != null) {
            messagingTemplate.convertAndSendToUser(
                    principal.getName(),
                    "/sub/errors",
                    ErrorResponse.of(e.getErrorCode().getMessage(), e.getErrorCode().name())
            );
        }
    }
}
