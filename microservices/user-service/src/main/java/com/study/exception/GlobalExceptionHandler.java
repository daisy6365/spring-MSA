package com.study.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException e) {
        log.error("비즈니스 로직 예외 반환. BusinessException = {}", e.getMessage());
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ExceptionResponse.of(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(CommunicationException.class)
    public ResponseEntity<ExceptionResponse> handleCommunicationException(CommunicationException e) {
        log.error("Microservice 통신 예외 반환. CommunicationException = {}", e.getMessage());
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ExceptionResponse.of(e.getErrorCode(), e.getMessage()));
    }

}
