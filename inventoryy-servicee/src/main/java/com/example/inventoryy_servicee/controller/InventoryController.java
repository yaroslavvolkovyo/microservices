package com.example.inventoryy_servicee.controller;

import com.example.inventoryy_servicee.dto.ProductInventoryDto;
import com.example.inventoryy_servicee.factory.InventoryDtoFactory;
import com.example.inventoryy_servicee.repository.InventoryRepository;
import com.example.inventoryy_servicee.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryDtoFactory inventoryDtoFactory;
    private final InventoryService inventoryService;

    //получение количества товара
    @GetMapping("/{article}")
    public int getQuantity(@PathVariable long article) {
        return  inventoryService.productInventoryQuantity(article);
    }

    //все продукты
    @GetMapping
    public List<ProductInventoryDto> getAllProducts() {
        return inventoryService.productsInventory().stream().map(inventoryDtoFactory::createProductInventoryDto).collect(Collectors.toList());
    }
}

