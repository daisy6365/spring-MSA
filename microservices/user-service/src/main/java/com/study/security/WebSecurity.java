package com.study.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    private final Environment env;
    private final AuthService authService;
    private final ObjectPostProcessor<Object> objectPostProcessor;

    private static final String[] PERMIT_ALL_URL = {
            "/users/**",
            "/",
            "/**"
    };

    /**
     * security 관련된 부분 한곳에 책임 분리
     * create passwordEncoder Bean
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // WebSecurityConfigurerAdapter, authorizeRequest() Deprecated됨
    // Bean에 등록하여 사용
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.csrf(AbstractHttpConfigurer::disable);
        http.csrf().disable();
        http.authorizeHttpRequests(request ->
        {
            try {
                request.requestMatchers(PERMIT_ALL_URL).permitAll() // 얘는 제외
                        .requestMatchers(new IpAddressMatcher("127.0.0.1")).authenticated() // 로컬요청 별도 인증
                        .and()
                        .addFilter(getAuthenticationFilter());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        return http.build();
    }

    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(passwordEncoder());
        return auth.build();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(builder), authService, env);
//        authenticationFilter.setAuthenticationManager(authenticationManager(builder));

        return authenticationFilter;
    }

}
