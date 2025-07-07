package com.example.inventoryy_servicee.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//это простое дто которое, для отправки в product-service через feign client
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryDto {
    private long article;
    private int quantity;
}
