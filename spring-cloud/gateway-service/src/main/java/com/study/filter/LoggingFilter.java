package com.study.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 사용자 정의 필터를 통해 로깅 작업을 상세하게 적용 가능함
 */
@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    // 필터 동작
    @Override
    public GatewayFilter apply(Config config) {
        // Logging Pre Filter
//        return ((exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Logging filter baseMessage: {}", config.getBaseMessage());
//
//            if (config.isPreLogger()) {
//                log.info("Logging filter Start: request id -> {}", request.getId());
//            }
//            // Logging Post Filter
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                if (config.isPostLogger()) {
//                    log.info("Global filter End: response code -> {}", response.getStatusCode());
//                }
//            }));
//        });

        /**
         * OrderedGatewayFilter : GatewayFilter를 구현한 자식 클래스
         * filter(ServerWebExchange exchange, GatewayFilterChain chain)
         * Webflux 환경이기 때문에 Servlet이 아닌 SeverWebExchange가 매개변수
         */
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging filter baseMessage: {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Logging Pre filter: request id -> {}", request.getId());
            }
            // Logging Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("Logging Post filter: response code -> {}", response.getStatusCode());
                }
            }));
        }, Ordered.HIGHEST_PRECEDENCE); // 필터의 우선 순위를 변경 가능
        // Ordered.LOWEST_PRECEDENCE : 가장 낮은 우선 순위
        // Ordered.HIGHEST_PRECEDENCE : 가장 높은 우선 순위

        return filter;
    }

    // application.yml에서 작성한 필터 파라미터
    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}