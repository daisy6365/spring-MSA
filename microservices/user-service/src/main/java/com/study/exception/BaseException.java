package com.study.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final int httpStatus;
    private final String errorCode;
    private final String message;

    protected BaseException(int httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
