package com.study.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.domain.CatalogEntity;
import com.study.domain.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CatalogRepository catalogRepository;

    /**
     * 상품 업데이트 토픽에 따라 데이터 반영
     */
    @KafkaListener(topics = "example-catalog-topic")
    public void updateQty(String kafkaMessage){
        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper(); // 데이터 매핑
        try {
            // 들어온 String 타입의 kafkaMessage를 파싱함
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        }catch (JsonProcessingException ex){
            ex.printStackTrace();
        }

        CatalogEntity entity = catalogRepository.findByProductId((String) map.get("productId"));
        if(entity != null){
            // 수량 업데이트
            entity.setStock(entity.getStock() - (Integer) map.get("qty"));
            catalogRepository.save(entity);
        }
    }
}