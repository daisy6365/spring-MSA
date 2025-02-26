package com.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * EnableEurekaServer? VS EnableDiscoveryClient?
 * EnableEurekaServer
 * - EurekaServer : 서비스 등록의 역할
 * - Microservice들이 자신의 위치를 등록 & 조회할 수 있도록 함
 *
 * EnableDiscoveryClient
 * - EurekaServer에 자신의 인스턴스를 등록
 * - 주기적으로 Heartbeat를 전송하여 인스턴스의 상태를 알림
 * - Microservice끼리 위치정보를 조회한 후, 어디로 요청을 보낼 지를 알아냄
 */
@SpringBootApplication
// Eureka 서버와 연결되는 Client
// 자신의 위치 정보를 Eureka 서버에 등록하고, 다른 서비스의 위치를 조회할 클라이언트에 선언
@EnableDiscoveryClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}


