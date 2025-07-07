package com.example.orderr_servicee.kafka;

import com.example.orderr_servicee.dto.OrderKafkaRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderKafkaProducer {
    private final KafkaTemplate<String, OrderKafkaRequest> kafkaTemplate;

    public void sendProductKafka(OrderKafkaRequest orderKafkaRequest) {
        kafkaTemplate.send("order-request-topic", orderKafkaRequest);
        log.info("Product sent to kafka: article={}", orderKafkaRequest.getArticle());
    }
}