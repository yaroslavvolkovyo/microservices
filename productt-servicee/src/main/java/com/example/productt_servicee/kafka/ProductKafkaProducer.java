package com.example.productt_servicee.kafka;

import com.example.productt_servicee.dto.ProductInventoryKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductKafkaProducer {
    private final KafkaTemplate<String, ProductInventoryKafka> kafkaTemplate;

    public void sendProductKafka(ProductInventoryKafka productRequestKafka) {
        kafkaTemplate.send("product-topic", productRequestKafka);
        log.info("Product sent to kafka: article={}", productRequestKafka.getArticle());
    }
}