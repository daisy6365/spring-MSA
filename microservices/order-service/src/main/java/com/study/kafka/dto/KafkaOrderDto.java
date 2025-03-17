package com.study.kafka.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class KafkaOrderDto implements Serializable {
    private Schema schema;
    private Payload payload;

    public static KafkaOrderDto from(Schema schema, Payload payload){
        KafkaOrderDto dto = new KafkaOrderDto();
        dto.schema = schema;
        dto.payload = payload;
        return dto;
    }
}
