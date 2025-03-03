package com.study.security;

import com.study.dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;

import java.util.Date;

public class JwtUtil {
    /**
     * JWT의 장점
     * 클라이언트 독립적인 서비스 -> stateless
     * CDN (Content Delivery Network) : 캐시서버와 인증처리 가능
     * No Cookie Session (No CSRF)
     *
     * + Method의 static 영역을 사용하여 공유성 메모리 영역을 만듦
     */
    public static String createToken(UserDto userDetails, Environment env){
        return Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();
    }
}
