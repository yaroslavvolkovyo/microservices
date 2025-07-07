package com.example.orderr_servicee.controller;

import com.example.orderr_servicee.dto.OrderKafkaRequest;
import com.example.orderr_servicee.dto.Status;
import com.example.orderr_servicee.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderKafkaRequest createOrder(@RequestBody OrderKafkaRequest orderKafkaRequest) {
        orderService.sendProductKafka(orderKafkaRequest);
        return orderKafkaRequest;
    }

    @GetMapping("/{orderId}")
    public Status getOrderId(@PathVariable Long orderId ) {
        return orderService.getStatus(orderId);
    }
}
