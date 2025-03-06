package com.study.controller;

import com.study.Greeting;
import com.study.domain.UserEntity;
import com.study.dto.UserDto;
import com.study.request.RequestUser;
import com.study.response.ResponseUser;
import com.study.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
// API gateway와 URI 조건을 맞춰줘야 함.
@RequestMapping("/")
public class UserController {

    // 인스턴스 생성 // yml파일에 존재하는 정보를 읽어옴
    private final Environment env;
    // yml파일에 존재하는 정보를 읽어옴 @Value로 대체 가능
    private final Greeting greeting;
    private final UserService userService;

    /**
     * before git commit
     * It's Working in User Service, port(local.server.port)=8081, port(server.port)=8081, token secret=null, token expiration time=null
     *
     * After git commit
     * It's Working in User Service, port(local.server.port)=8081, port(server.port)=8081,
     * token secret=Wm7dP4x9vTgH3bM1Kq8XzJ2pRsY5LNv6aY43D254tVjC59874209qZfG7wM8523hBrTkN51252234Q134PxYVKH5Zd, token expiration time=86400000
     */
    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service"
                //
                + ", port(local.server.port)="+ env.getProperty("local.server.port")
                // application.yml 에 저장된 정보들
                + ", port(server.port)="+ env.getProperty("server.port")
                + ", token secret="+ env.getProperty("token.secret")
                + ", token expiration time="+ env.getProperty("token.expiration_time"));
    }

    @GetMapping("/welcome")
    public String welcome(){
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser request){
        // Json 형태로 Client가 요청 정보를 전달
        // ModelMapper 매핑 전략 -> 일치해야 함
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 비지니스 로직 수행 하면서 UserDto로 전환
        // 곧바로 DB에 반영하기 위해 UserEntity은로 전환 후 insert
        UserDto userDto = modelMapper.map(request, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
    // 한명 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserDto user = userService.getUserByUserId(userId);

        ResponseUser result = new ModelMapper().map(user, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 전체조회
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();

        userList.forEach( v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
