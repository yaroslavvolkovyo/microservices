package com.example.inventoryy_servicee.service;

import com.example.inventoryy_servicee.dto.OrderKafkaRequest;
import com.example.inventoryy_servicee.dto.OrderKafkaResponse;
import com.example.inventoryy_servicee.dto.ProductInventoryKafka;
import com.example.inventoryy_servicee.dto.Status;
import com.example.inventoryy_servicee.factory.InventoryDtoFactory;
import com.example.inventoryy_servicee.kafka.OrderKafkaProducer;
import com.example.inventoryy_servicee.model.ProductInventory;
import com.example.inventoryy_servicee.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryDtoFactory inventoryDtoFactory;
    private final InventoryRepository inventoryRepository;
    private final OrderKafkaProducer orderKafkaProducer;
    //получение количества по артикулу
    public int productInventoryQuantity(long article) {
        return inventoryRepository.productInventoryQuantityByArticle(article);
    }
    //все продукты
    public List<ProductInventory> productsInventory() {
        return inventoryRepository.findAll();
    }

    //пришел заказ
    @KafkaListener(topics = "order-request-topic",
            groupId = "order-group",
            containerFactory = "orderRequestKafkaListenerFactory")
    public void listenOrderRequest(OrderKafkaRequest orderKafkaRequest) {
        log.info("приехал товарчик");
        createOrder(orderKafkaRequest);
    }

    @Transactional
    public void createOrder(OrderKafkaRequest orderKafkaRequest) {
        OrderKafkaResponse orderKafkaResponse = new OrderKafkaResponse();
        if(inventoryRepository.findByArticle(orderKafkaRequest.getArticle()) == null){
            orderKafkaResponse.setArticle(orderKafkaRequest.getArticle());
            orderKafkaResponse.setOrderId(orderKafkaRequest.getOrderId());
            orderKafkaResponse.setQuantity(orderKafkaRequest.getQuantity());
            orderKafkaResponse.setStatus(Status.FAILED);
            orderKafkaProducer.sendProductKafka(orderKafkaResponse);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "товара по такому артикулу нет в наличии");
        }

        if(inventoryRepository.productInventoryQuantityByArticle(orderKafkaRequest.getArticle()) <= 0){
            orderKafkaResponse.setArticle(orderKafkaRequest.getArticle());
            orderKafkaResponse.setOrderId(orderKafkaRequest.getOrderId());
            orderKafkaResponse.setQuantity(orderKafkaRequest.getQuantity());
            orderKafkaResponse.setStatus(Status.FAILED);
            orderKafkaProducer.sendProductKafka(orderKafkaResponse);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "ТОВАРА НЕТ В НАЛИЧИИ");
        }
        int quantity = inventoryRepository.productInventoryQuantityByArticle(orderKafkaRequest.getArticle()) - orderKafkaRequest.getQuantity();
        inventoryRepository.updateQuantity(orderKafkaRequest.getArticle(), quantity);
        orderKafkaResponse.setArticle(orderKafkaRequest.getArticle());
        orderKafkaResponse.setOrderId(orderKafkaRequest.getOrderId());
        orderKafkaResponse.setQuantity(quantity);
        orderKafkaResponse.setStatus(Status.CREATED);
        orderKafkaProducer.sendProductKafka(orderKafkaResponse);
        log.info("СЕЙЧАС ТОВАРЧИКА={}" , inventoryRepository.productInventoryQuantityByArticle(orderKafkaRequest.getArticle()) );
    }

    //от кафки приходит товар
    @KafkaListener(topics = "product-topic",
            groupId = "warehouse-group",
            containerFactory = "productInventoryKafkaListenerFactory")
    @Transactional // Добавляем транзакцию на уровне listener'а
    public void consumeOrder(ProductInventoryKafka productInventoryKafka) {
        processProduct(productInventoryKafka);
    }

    //создание товара
    public void processProduct(ProductInventoryKafka productKafka) {
        log.info("Processing update for article: {}", productKafka.getArticle());

        int updatedRows = inventoryRepository.productInventoryQuantityByArticle(productKafka.getArticle());

        if (updatedRows == 0) {
            // Если запись не найдена, создаем новую
            ProductInventory newEntity = new ProductInventory();
            newEntity.setArticle(productKafka.getArticle());
            newEntity.setQuantity(productKafka.getQuantity());
            inventoryRepository.save(newEntity);
            log.info("Created new inventory record");
        } else {
            updatedRows += productKafka.getQuantity();
            inventoryRepository.updateQuantity(productKafka.getArticle(), updatedRows);
            log.info("ОБНОВЛЕНО {} в инвенторе щас", updatedRows);
        }
    }
}
