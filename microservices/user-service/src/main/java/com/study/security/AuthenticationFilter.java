package com.study.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.dto.UserDto;
import com.study.request.RequestLogin;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

/**
 * AbstractAuthenticationProcessingFilter와 UsernamePasswordAuthenticationFilter의 차이점
 * 둘은 Security와 관련하여 상속관계임. 전자가 부모
 * --------------------------------
 * AbstractAuthenticationProcessingFilter
 * - 더 유연한 인증 방식 구현 가능
 * - OAuth2, JWT Authorization 등
 * - 요청 path 패턴을 직접 설정해야 함
 *
 * UsernamePasswordAuthenticationFilter
 * - Auth 관련 기본 제공 기능이 많아서 구현하는데 편리, BUT 자율성이 적음
 * - 기본적으로 "POST /login" 을 자동으로 처리
 * -
 */
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthService authService;
    private Environment env;

    // 부모가 갖고 있는 authenticationManager에 내가 원하는 인증 설정을 넣음
    public AuthenticationFilter(AuthenticationManager authenticationManager, AuthService authService, Environment env) {
        super(authenticationManager);
        this.authService = authService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청 HttpServletRequest를 받아서 자동으로 RequestLogin으로 매핑
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            /**
             * new UsernamePasswordAuthenticationToken
             * email, password 를 spring security 내에서 인증객체로 이해할 수 있도록 변환해야함
             *
             * new ArrayList<>()
             * 어떤 권한들을 가질 것인지
             *
             */
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // super.successfulAuthentication(request, response, chain, authResult);
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = authService.getUserDetailsByEmail(username);

        log.debug("Authentication successful, user info = {}", username);
    }
}
