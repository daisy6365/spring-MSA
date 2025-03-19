package com.study.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4JConfig {
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration(){
        //
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                // CircuitBreaker를 열지 결정하는 failure rate Threshold percentage
                .failureRateThreshold(4)
                // CircuitBreader open 상태를 유지하는 지속 기간
                // 해당 기간 후에 Half-open 상태
                .waitDurationInOpenState(Duration.ofMillis(1000))
                // CircuitBreaker가 close할때 결과 기록함.
                // 이때 사용되는 슬라이딩 창의 유형
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                // Close할때 , 호출 결과를 기록함
                // 이때 사용되는 슬라이딩 창의 크기 구성
                .slidingWindowSize(2)
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                // TimeLimiter는 future supplier의 시간 제한을 정하는 api
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        // Resilience4JCircuitBreakerFactory에 대한 설정 반환
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }
}
