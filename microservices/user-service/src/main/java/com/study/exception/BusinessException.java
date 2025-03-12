package com.study.exception;

import org.springframework.http.HttpStatusCode;

public class BusinessException extends BaseException {
    protected BusinessException(HttpStatusCode httpStatus, String errorCode, String message) {
        super(httpStatus.value(), errorCode, message);
    }
}
