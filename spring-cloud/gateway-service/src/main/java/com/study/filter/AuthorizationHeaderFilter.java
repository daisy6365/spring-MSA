package com.study.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * login -> token
 * ysers (with token) -> Header(include token)
 */
@Slf4j
@Component
/**
 * @RequiredArgsConstructor 사용하면 안됨!!!!
 * 왜?
 * 내가 "선언한" 멤버 변수에 대한 생성자만 생성
 * BUT 부모클래스인 AbstractGatewayFilterFactory<T>는 Config.class가 필요함
 * -> 명시적으로 호출 필요
 *
 * 자동으로 만들어주는 @RequiredArgsConstructor이 아닌 직접적으로 config까지 호출해줘야함
 * -> AuthenticationFilter 구현도 비슷한 원리
 */
//@RequiredArgsConstructor
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    private final Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    public static class Config {
    }


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

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;
        try {
            subject = Jwts.parser()
                    .setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
        } catch (Exception ex) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()){
            returnValue = false;
        }

        return returnValue;
    }

    /**
     * Mono?
     * Spring WebFlux에서 사용하는 비동기 단일 값 컨테이너
     * - 비동기 , 논블로킹(바로 다음 작업을 수행함)
     * - 데이터가 0개 또는 1개 (Mono<T> -> T 타입 값 1개 또는 없음)
     * - subscribe() 하기전에는 실행되지 않음
     * -> 실행 트리거 역할. Lazy 방식에서 사용
     * -> 비동기 도중에 에러가 발생하면, 내부에서 onError()를 실행!
     * -> 그래서 아래와 같이 에러처리에 대한 메소드 컨벤션이 이런듯
     *
     * Mono vs Flux
     * - 비동기 데이터를 다루는 두가지 단위 타입
     * Mono : 저장가능한 데이터 개수 최대 1개
     *  -> 단일값 반환시 사용
     * Flux : 저장가능한 데이터 개수 N개, 스트림 형태
     *  -> 실시간 스트리밍 데이터, 여러개의 값
     */
    private Mono<Void> onError(ServerWebExchange exchange,
                               String error,
                               HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(error);
        return response.setComplete();
    }
}
