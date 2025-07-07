package com.example.inventoryy_servicee.factory;

import com.example.inventoryy_servicee.dto.ProductInventoryDto;
import com.example.inventoryy_servicee.dto.ProductInventoryKafka;
import com.example.inventoryy_servicee.model.ProductInventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryDtoFactory {
    //из кафки в обычный
    public ProductInventory createProductInventory(ProductInventoryKafka productInventory) {
        ProductInventory productInventoryDto = new ProductInventory();
        productInventoryDto.setArticle(productInventory.getArticle());
        productInventoryDto.setQuantity(productInventory.getQuantity());
        return productInventoryDto;
    }

    //из обычной в дто
    public ProductInventoryDto createProductInventoryDto(ProductInventory productInventory) {
        ProductInventoryDto productInventoryDto = new ProductInventoryDto();
        productInventoryDto.setArticle(productInventory.getArticle());
        productInventoryDto.setQuantity(productInventory.getQuantity());
        return productInventoryDto;
    }
}
