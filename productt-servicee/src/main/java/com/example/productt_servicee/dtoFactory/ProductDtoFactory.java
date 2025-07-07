package com.example.productt_servicee.dtoFactory;

import com.example.productt_servicee.dto.ProductDetailsResponse;
import com.example.productt_servicee.dto.ProductInventoryKafka;
import com.example.productt_servicee.dto.ProductRequest;
import com.example.productt_servicee.dto.ProductShortResponse;
import com.example.productt_servicee.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoFactory {
    //создает "короткую" сущность для момента когда пользователь хочет посмотреть все товары
    public ProductShortResponse createProductShortResponse(Product product) {
        ProductShortResponse productShortResponse = new ProductShortResponse();
        productShortResponse.setArticle(product.getArticle());
        productShortResponse.setName(product.getName());
        productShortResponse.setPrice(product.getPrice());
        productShortResponse.setCategory(product.getCategory());
        return productShortResponse;
    }

    //когда автор продукта хочет создать свой продукт
    public Product createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setQuantity(productRequest.getQuantity());
        if(product.getQuantity() == 0 ){
            product.setAvailable(false);
        }else {
            product.setAvailable(true);
        }
        return product;
    }

    //когда пользователь выбирает конкретный продукт
    public ProductDetailsResponse createProductDetailsResponse(Product product) {
        ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse();
        productDetailsResponse.setArticle(product.getArticle());
        productDetailsResponse.setName(product.getName());
        productDetailsResponse.setPrice(product.getPrice());
        productDetailsResponse.setCategory(product.getCategory());
        productDetailsResponse.setQuantity(product.getQuantity());
        return productDetailsResponse;
    }
    //для отправки в kafka
    public ProductInventoryKafka createProductRequestKafka(Product product) {
        ProductInventoryKafka productRequestKafka = new ProductInventoryKafka();
        productRequestKafka.setArticle(product.getArticle());
        productRequestKafka.setQuantity(product.getQuantity());
        return productRequestKafka;
    }
}
