package com.study.client;

import com.study.response.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * FeignClientsRegistrar에 의해 Bean으로 등록됨
 * spring이 JDK 동적 프록시 (Dynamic Proxy)를 생성
 * -> Feign HTTP 요청을 수행하는 객체를 만듦
 * -> 인터페이스 호출 시 HTTP 요청 처리
 *
 * FeignClientFactoryBean이 HTTP 요청을 보내는 Proxy 객체를 Bean으로 등록
 */
@FeignClient(name = "order-service") // Microservice 이름
public interface OrderServiceClient {
    @GetMapping("/order-service/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable String userId);

    @GetMapping("/order-service/{userId}/orders_error")
    List<ResponseOrder> getOrdersError(@PathVariable String userId);
}
