package com.study.kafka.dto;

import lombok.Data;

@Data
public class Payload {
    private String order_id;
    private String user_id;
    private String product_id;
    private int qty;
    private int unit_price;
    private int total_price;

    public static Payload from(String order_id, String user_id, String product_id, int qty, int unit_price, int total_price){
        Payload payload = new Payload();

        payload.order_id = order_id;
        payload.user_id = user_id;
        payload.product_id = product_id;
        payload.qty = qty;
        payload.unit_price = unit_price;
        payload.total_price = total_price;

        return payload;
    }
}
