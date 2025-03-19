package com.study.controller;

import com.study.domain.OrderEntity;
import com.study.dto.OrderDto;
import com.study.kafka.KafkaProducer;
import com.study.kafka.messagequeue.OrderProducer;
import com.study.request.RequestOrder;
import com.study.response.ResponseOrder;
import com.study.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {
    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health_check")
    public String status(){
        // port 번호 알려줌
        return String.format("It's Working in User Service on PORT %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder request){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(request, OrderDto.class);
        orderDto.setUserId(userId);

        OrderDto result = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = modelMapper.map(result, ResponseOrder.class);

        /* kafka */
//        orderDto.setOrderId(UUID.randomUUID().toString());
//        orderDto.setTotalPrice(request.getQty() * request.getUnitPrice());

        /* send this order data to the kafka */
        // 여러개의 서비스 구동중 일때, 데이터 분산 저장 -> 독립적인 데이터베이스를 가짐
        // -> 데이터 동기화에 대한 문제 발생 -> kafka message server를 통해 해결
//        kafkaProducer.send("example-catalog-topic", orderDto);
//        orderProducer.send("orders", orderDto);

//        ResponseOrder responseOrder = modelMapper.map(orderDto, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseOrder> getOrder(@PathVariable("orderId") String orderId){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto result = orderService.getOrderByOrderId(orderId);
        ResponseOrder responseOrder = modelMapper.map(result, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseOrder);
    }

    // 사용자가 주문한 상품 리스트 조회
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId){
        Iterable<OrderEntity> orderEntities = orderService.getOrderByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderEntities.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}



