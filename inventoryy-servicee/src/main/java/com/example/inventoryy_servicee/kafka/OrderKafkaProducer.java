package com.example.inventoryy_servicee.kafka;

import com.example.inventoryy_servicee.dto.OrderKafkaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderKafkaProducer {
    private final KafkaTemplate<String, OrderKafkaResponse> kafkaTemplate;

    public void sendProductKafka(OrderKafkaResponse orderKafkaResponse) {
        kafkaTemplate.send("order-response-topic", orderKafkaResponse);
        log.info("Product sent to kafka: article={}", orderKafkaResponse.getArticle());
    }
}