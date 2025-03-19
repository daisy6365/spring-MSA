package com.study.service;

import com.study.client.OrderServiceClient;
import com.study.domain.UserEntity;
import com.study.domain.UserRepository;
import com.study.dto.UserDto;
import com.study.response.ResponseOrder;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final Environment env;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptePwd(bCryptPasswordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        UserDto result = modelMapper.map(userEntity, UserDto.class);
        return result;
    }

    // 한명 조회
    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = new ModelMapper().map(user, UserDto.class);


        /* Using as RestTemplate
        // order url의 주소를 yml로 부터 가져옴
        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
        ResponseEntity<List<ResponseOrder>> response = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ResponseOrder>>() {});

        List<ResponseOrder> orderList = response.getBody();
        userDto.setOrderList(orderList); */

        /* Using as Feign client */
//        List<ResponseOrder> orderList = new ArrayList<>();
//        try {
//            orderList = orderServiceClient.getOrdersError(userId);
//        }
//        catch (FeignException e){
//            log.debug(e.getMessage());
//        }

        /* CircuitBreaker */
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> orderList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
                // 문제가 발생했을 시?, 빈 ArrayList 던짐
                throwable -> new ArrayList<>());

        /* Using ErrorDecoder */
        // try-catch문을 사용해 예외를 처리할 필요가 없음
//        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
        userDto.setOrderList(orderList);

        return userDto;
    }

    // 전체 조회
    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }
}

