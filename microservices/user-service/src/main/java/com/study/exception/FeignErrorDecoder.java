package com.study.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {
    private final Environment env;

    /**
     * @ spring boot 버전 변경에 따른 변경사항 @
     * [이전 버전]
     * BasicErrorController(기본적인 오류 처리). getErrorAttributes()
     * -> DefaultErrorAttributes (에러 정보를 제공)
     * -> ResponseStatusException을 JSON으로 변환
     * -> "getReason()값을 응답에 추가함"
     *
     * [spring boot 3.x]
     * @ControllerAdvice의 강화
     * ErrorAtrttributes를 더 세밀하게 커스터마이징 할수 있도록 변경
     * 유연하게 세밀하게 개선될 수 있도록 변경 됨
     * 따라서 내가 설젇한 메세지는 안나오는것.
     *
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400: {
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
            }
            case 404: {
                if(methodKey.contains("getOrders")){
                    return new CommunicationException(HttpStatusCode.valueOf(response.status()),
                            "0001", env.getProperty("order_service.exception.order_is_not_exist"));
                }
                break;
            }
            default: {
                return new CommunicationException(HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                        "0000","communication system error");
            }
        }
        return null;
    }
}
