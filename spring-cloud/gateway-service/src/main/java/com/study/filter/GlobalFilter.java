package com.study.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 어떤 요청 EndPoint가 와도, 공통적으로 수행되는 필터
 * application.yml에도 한번에 설정 가능
 */
@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter(){
        super(Config.class);
    }

    // 필터 동작
    @Override
    public GatewayFilter apply(Config config) {
        // Global Pre Filter
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global filter baseMessage: {}", config.getBaseMessage());

            // @Getter를 통해서 가져온 것임
            if (config.isPreLogger()) {
                log.info("Global filter Start: request id -> {}", request.getId());
            }
            // Global Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("Global filter End: response code -> {}", response.getStatusCode());
                }
            }));
        });
    }

    /**
     * 필요한 매개변수의 값는 application.yml파일에 설정 제어 가능
     * @Setter @Getter를 위해 @Data 해놓음
     *
     * 단점 : 설정정보가 APPlication.yml에 존재 한다?
     * 수정 시 값을 변경하고 다시 빌드하는 번거로움을 진행해야 함
     */
    @Data
    public static class Config{
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
