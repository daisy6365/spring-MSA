package com.study.kafka.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.dto.OrderDto;
import com.study.kafka.dto.Field;
import com.study.kafka.dto.KafkaOrderDto;
import com.study.kafka.dto.Payload;
import com.study.kafka.dto.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    List<Field> fieldList = Arrays.asList(
            new Field("string", true, "order_id"),
            new Field("string", true, "user_id"),
            new Field("string", true, "product_id"),
            new Field("int32", true, "qty"),
            new Field("int32", true, "unit_price"),
            new Field("int32", true, "total_price")
    );

    Schema schema = Schema.from("struct", fieldList, false, "orders");

    /**
     * OrderService로 부터 요청된 주문정보를 "OrderService의 DB"에 바로 전송 X
     * -> Kafka Topic에 전송
     *
     * Kafka Topic에 설정된 Kafka Sink connector를 통해
     * 단일 DB에 저장 및 데이터 동기화
     */
    public OrderDto send(String topic, OrderDto order) {
        Payload payload = Payload.from(
                order.getOrderId(),
                order.getUserId(),
                order.getProductId(),
                order.getQty(),
                order.getUnitPrice(),
                order.getTotalPrice()
        );

        KafkaOrderDto kafkaOrderDto = KafkaOrderDto.from(schema, payload);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Order Producer sent data from the Order microservice = {}", topic);

        return order;
    }
}
