package com.study.kafka.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Schema {
    private String type;
    private List<Field> fields = new ArrayList<>();
    private boolean optional;
    private String name;

    public static Schema from(String type, List<Field> fields, boolean optional, String name){
        Schema schema = new Schema();
        schema.type = type;
        schema.fields = fields;
        schema.optional = optional;
        schema.name = name;

        return schema;
    }
}
