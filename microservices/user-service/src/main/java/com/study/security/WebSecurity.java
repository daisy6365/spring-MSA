package com.study.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    // WebSecurityConfigurerAdapter, authorizeRequest() Deprecated됨
    // Bean에 등록하여 사용
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests().requestMatchers("/user-service/**").permitAll();
        http.authorizeHttpRequests().requestMatchers("/user-service/**").permitAll();
        http.headers().frameOptions().disable();

        return http.build();
    }

}
