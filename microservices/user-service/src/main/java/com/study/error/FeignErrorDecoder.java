package com.study.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * ErrorDecoder
 * feign client에서 제공해줌
 * 에러 상태코드에 따라 분기하여 작업을 수행
 *
 * 왜? 이걸 해야해?
 * Microsevice간 통신 하는 과정에서, 에러발생하면 무조건 500에러가 발생함
 * 404에러임에도 불구하고 500이 도출되는 것을 제어하기 위함
 */
@Component
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                break;
            case 404: {
                if(methodKey.contains("getOrders")){
                    return new ResponseStatusException(HttpStatusCode.valueOf(response.status()),
                            "User's order is not exist.");
                }
                break;
            }
            default: {
                return new Exception(response.reason());
            }
        }
        return null;
    }
}
