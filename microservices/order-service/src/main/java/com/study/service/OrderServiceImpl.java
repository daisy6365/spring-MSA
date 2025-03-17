package com.study.service;

import com.study.domain.OrderEntity;
import com.study.domain.OrderRepository;
import com.study.dto.OrderDto;
import com.study.kafka.KafkaProducer;
import com.study.kafka.messagequeue.OrderProducer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto request) {
        request.setOrderId(UUID.randomUUID().toString());
        request.setTotalPrice(request.getQty() * request.getUnitPrice());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = modelMapper.map(request, OrderEntity.class);

        orderRepository.save(orderEntity);

        OrderDto result = modelMapper.map(orderEntity, OrderDto.class);

        return result;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto result = new ModelMapper().map(orderEntity, OrderDto.class);

        return result;
    }

    @Override
    public Iterable<OrderEntity> getOrderByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
