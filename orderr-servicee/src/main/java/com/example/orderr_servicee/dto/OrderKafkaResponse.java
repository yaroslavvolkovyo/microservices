package com.example.orderr_servicee.dto;

import lombok.Data;

@Data
public class OrderKafkaResponse {
    private long orderId;
    private long article;
    private int quantity;
    private Status status;
}
