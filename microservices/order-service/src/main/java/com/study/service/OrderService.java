package com.study.service;

import com.study.domain.OrderEntity;
import com.study.dto.OrderDto;

public interface OrderService {
    OrderDto createOrder(OrderDto request);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrderByUserId(String userId);
}
