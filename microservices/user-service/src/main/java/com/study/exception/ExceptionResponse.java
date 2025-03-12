package com.study.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private String code;
    private String message;

    // 정적팩터리 메서드를 통해 생성자 박살
    public static ExceptionResponse of(String code, String message) {
        ExceptionResponse response = new ExceptionResponse();
        response.code = code;
        response.message = message;
        return response;
    }
}
