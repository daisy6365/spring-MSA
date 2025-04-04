package com.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// 주소를 저장해 놓는 곳 (주소검색 시스템)
@SpringBootApplication
// EurekaServer로 동작할 수 있음
// Auto-Configuration을 통해 EurekaServer 관련 bean 등록
@EnableEurekaServer
public class DiscoveryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class, args);
    }
}
