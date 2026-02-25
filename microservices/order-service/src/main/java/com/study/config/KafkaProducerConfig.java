package com.study.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * OrderService -> CatalogService
 * 1. 주문이 들어옴
 * 2. Kafka 메세지 발행 설정
 */
@EnableKafka
@Configuration
public class KafkaProducerConfig {

    /**
     * Producer 설정 조립
     * DefaultKafkaProducerFactory 을 통해 기본적인 설정 Map<String, Object> 형태로 묶음
     * 반환 타입은 자유롭게 설정 가능. -> 편의성을 위해 writeValueAsString 으로 변경
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(properties);
    }

    /**
     * Spring 기반 데이터 전달 인스턴스
     * Producer 객체를 감싼 전송용 Template
     *
     * KafkaResourceHolder<K, V> holder = ProducerFactoryUtils
     * 					.getTransactionalResourceHolder(this.producerFactory, this.transactionIdPrefix, this.closeTimeout);
     * spring 내부에서 직접 Producer 리소스를 관리함
     * -> new KafkaProducer() 안해도 됨
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        // Producer 설정을 KafkaTemplate에 등록
        return new KafkaTemplate<>(producerFactory());
    }

}
