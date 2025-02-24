package com.study.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * CustomFilter. 사용자 정의 필터
 * 인증처리 및 로깅, 언어변경 등
 *
 * AbstractGatewayFilterFactory 상속
 * CustomFilter.Config 매개변수
 */
@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter(){
        super(Config.class);
    }


    /**
     * GatewayFilter 반환, 어떠한 작업을 할 것인지 정의
     */
    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter. 사전 필터(요청 전달 전)
        // Suppose we can extract JWT or Authentication
        return ((exchange, chain) -> {
            // 요청/응답 객체 조회 가능
            // netty 라는 내장 서버 - 비동기 방식
            // 때문에 Servelt이 아닌 "Server" 객체로 요청/응답 값을 가져옴
            // http.server.reactive 내에 존재하는 ServerHttp 객체 여야 함
            // --> Webflux를 지원해 주는 기능
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE filter: request id -> {}", request.getId());

            // Custom Post Filter. 사후 필터 (응답 반환 후)
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                // filter 정의 완료 후 , 반환값
                log.info("Custom POST filter: response -> {}", response.getStatusCode());
            }));
        });
    }

    public static class Config{
        // Put the Configuration properties
        // Configuration properties 정보 넣을 수 있음
    }
}
