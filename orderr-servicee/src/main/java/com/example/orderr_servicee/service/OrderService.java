package com.example.orderr_servicee.service;

import com.example.orderr_servicee.dto.OrderKafkaRequest;
import com.example.orderr_servicee.dto.OrderKafkaResponse;
import com.example.orderr_servicee.dto.Status;
import com.example.orderr_servicee.entity.Order;
import com.example.orderr_servicee.kafka.OrderKafkaProducer;
import com.example.orderr_servicee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderKafkaProducer orderKafkaProducer;

    public void sendProductKafka(OrderKafkaRequest orderKafkaRequest) {
        orderKafkaProducer.sendProductKafka(orderKafkaRequest);
        log.info("Продукт отправился в кафку из сервиса article={}", orderKafkaRequest.getArticle());

    }

    @KafkaListener(topics = "order-response-topic",
            groupId = "order-group",
            containerFactory = "orderRequestKafkaListenerFactory")
    public Status getStatus(OrderKafkaResponse orderKafkaResponse) {
        Order order = new Order();
        if (orderKafkaResponse.getStatus().equals(Status.CREATED)) {
            order.setArticle(orderKafkaResponse.getArticle());
            order.setQuantity(orderKafkaResponse.getQuantity());
            order.setStatus(orderKafkaResponse.getStatus());
            order.setOrderId(orderKafkaResponse.getOrderId());
            orderRepository.save(order);
            return Status.CREATED;
        }else {
            return Status.FAILED;
        }
    }

    public Status getStatus(Long orderId) {
        Status status = orderRepository.findStatusByOrderId(orderId);
        if (status == null) {
            return Status.FAILED;
        }
        else {
            return Status.CREATED;
        }
    }
}
