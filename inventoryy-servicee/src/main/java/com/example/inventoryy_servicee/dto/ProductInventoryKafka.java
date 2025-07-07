package com.example.inventoryy_servicee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//это дто для принятия кафкой в inventory-service для создания товара
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryKafka {
    private long article;
    private int quantity;
}
