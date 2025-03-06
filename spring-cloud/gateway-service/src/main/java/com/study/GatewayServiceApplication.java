package com.study;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    /**
     * spring 2.x 버전에는 @Bean으로 빈을 등록하도록 함
     * 하지만 spring 3.x 버전에는
     * InMemoryHttpTraceRepository deprecated.
     * actuator의 tracing 기능이 Micrometer기반의 HttpExchangeRepository 대체
     */
//    @Bean
//    public HttpTraceRepository httpTraceRepository(){
//        return new InMemoryHttpTraceRepository();
//    }
}