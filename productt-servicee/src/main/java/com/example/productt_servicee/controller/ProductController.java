package com.example.productt_servicee.controller;

import com.example.productt_servicee.dto.ProductDetailsResponse;
import com.example.productt_servicee.dto.ProductRequest;
import com.example.productt_servicee.dto.ProductShortResponse;
import com.example.productt_servicee.dtoFactory.ProductDtoFactory;
import com.example.productt_servicee.model.Category;
import com.example.productt_servicee.model.Product;
import com.example.productt_servicee.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/product")
public class ProductController {
    private final ProductDtoFactory productDtoFactory;
    private final ProductService productService;

    @GetMapping
    public List<ProductShortResponse> findAllShortProducts() {
        return productService.findAllProducts().stream().map(product -> productDtoFactory.createProductShortResponse(product)).collect(Collectors.toList());
    }
    @PostMapping
    public ProductDetailsResponse createProduct(@RequestBody ProductRequest productRequest) {
        Product product = productService.createProduct(productDtoFactory.createProduct(productRequest));
        return productDtoFactory.createProductDetailsResponse(product);
    }
    @GetMapping("/admin")
    public List<Product> findAll() {
        return productService.findAll();
    }
    @GetMapping("/{article}")
    public ProductDetailsResponse findProductByArticle(@PathVariable long article) {
        return productDtoFactory.createProductDetailsResponse(productService.findProductByArticle(article));
    }

    @GetMapping("/category")
    public List<ProductShortResponse> findAllProductsByCategory(@RequestParam Category category, @RequestParam(required = false) Integer sort) {
        return productService.findAllProductsByCategory(category, sort).stream().map(product -> productDtoFactory.createProductShortResponse(product)).collect(Collectors.toList());
    }

    @PostMapping ("/{article}")
    public ProductDetailsResponse updateProduct(@PathVariable long article, @RequestParam int quantity) {
        return productDtoFactory.createProductDetailsResponse(productService.addQuantityToProduct(article, quantity));
    }

}
