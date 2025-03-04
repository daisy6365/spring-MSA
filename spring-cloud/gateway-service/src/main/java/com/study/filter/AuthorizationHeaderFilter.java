package com.study.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * login -> token
 * ysers (with token) -> Header(include token)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    private final Environment env;

    public static class Config {}


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Header에 인증 값이 존재하는지 확인
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "No Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            // 존재하면 token 값 가져오기
            // 배열 형태로 오기 때문에 0번째를 가져옴
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authorizationHeader.replace("Bearer", "");

            if(!isJwtValid(token)){
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }


            return chain.filter(exchange);
        });
    }

    private boolean isJwtValid(String token){
        return false;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String no_authorization_header, HttpStatus unauthorized){
        return null;
    }



}
