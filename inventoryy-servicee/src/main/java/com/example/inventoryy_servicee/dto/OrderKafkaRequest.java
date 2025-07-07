package com.example.inventoryy_servicee.dto;

import lombok.Data;

@Data
public class OrderKafkaRequest {
    private long orderId;
    private long article;
    private int quantity;
}
