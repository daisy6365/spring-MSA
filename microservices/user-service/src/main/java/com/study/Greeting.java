package com.study;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 빈등록
@Component
@Data
//@AllArgsConstructor // Arguments인 생성자 모든걸 만들어줌
//@NoArgsConstructor // Arguments가 아닌 default 생성자를 만들어줌
public class Greeting {

    //application.yml에 있는 설정을 가져옴
    @Value("${greeting.message}")
    private String message;
}
