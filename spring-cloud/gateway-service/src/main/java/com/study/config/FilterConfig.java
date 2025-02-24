package com.study.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
    /**
     * ex. /user-service/** 라는 이름으로 요청이 들어옴
     * gateway > filterconfig에서 받아서
     * request Header와 response Header에 값을 각각 추가
     *
     * 이렇게 해서 각 특정 라우트마다 application.yml과 같은 설정을 할 수 있음
     */
//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                // 라우트 정보 추가
                .route(route -> route
                        // Route 등록
                        .path("/user-service/**")
                        // 필터를 어떻게 사용할 것인지 정의할 수 있음
                        // Path 확인 후 알맞은 필터 적용
                        .filters(filter -> filter
                                .addRequestHeader("user-request", "user-request-header")
                                .addResponseHeader("user-response", "user-response-header"))
                        .uri("http://localhost:8081"))
                .build(); // 라우트에 필요한 정보들을 메모리에 반영
    }
}
