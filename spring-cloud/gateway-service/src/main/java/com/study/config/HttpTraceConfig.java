package com.study.config;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpTraceConfig {

    /**
     * 메모리기반 Http 요청 저장소
     *
     * 동작방식
     * - Client가 요청을 보냄
     * - HttpExchangesFilter내부 클래스에서 요청을 기록하고 HttpExchange 객체를 생성하여 저장
     * - 기본적으로 In-Memory 저장소를 사용하지만, 개발자가 커스텀하여 DB에 저장할 수 있음
     */
    @Bean
    public HttpExchangeRepository httpExchangeRepository() {
        // 최근 100개의 요청정보를 저장하는 구현체
        // capacity = 100;
        return new InMemoryHttpExchangeRepository();
    }
}
