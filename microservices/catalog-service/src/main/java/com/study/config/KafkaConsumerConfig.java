package com.study.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka // kafka에서 사용 할 수 있도록
@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    // 접속할 수 있는 Consumer 설정
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        // bootstrap servers
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        // group id consumer들을 그룹화할 수 있음
        // -> 대규모 데이터를 병렬적으로 처리
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId");
        // key deserializer
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // value deserializer
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    /**
     * Listener 등록
     * 내부적으로 ConcurrentMessageListenerContainer를 만들어서
     * Topic 구독 -> polling loop -> 메세지 콜백 호출 ((@KafkaListener)
     *
     * polling loop
     * KafkaMessageListenerContainer 내부에서 반복적으로 poll()하여 새메세지를 기다림
     *
     * <String, String>으로 전송했으니, <String, String>로 받음
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 위에 설정한 Consumer 정보를 Listener에 등록
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
