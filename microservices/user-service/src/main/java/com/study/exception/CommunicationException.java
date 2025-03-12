package com.study.exception;

import org.springframework.http.HttpStatusCode;

public class CommunicationException extends BaseException {
    protected CommunicationException(HttpStatusCode httpStatus, String errorCode, String message) {
        super(httpStatus.value(), errorCode, message);
    }

}
