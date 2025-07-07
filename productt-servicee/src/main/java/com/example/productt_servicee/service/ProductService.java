package com.example.productt_servicee.service;

import com.example.productt_servicee.dto.ProductRequest;
import com.example.productt_servicee.dto.ProductShortResponse;
import com.example.productt_servicee.dtoFactory.ProductDtoFactory;
import com.example.productt_servicee.feignClient.FeignClientInventoryService;
import com.example.productt_servicee.kafka.ProductKafkaProducer;
import com.example.productt_servicee.model.Category;
import com.example.productt_servicee.model.Product;
import com.example.productt_servicee.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "products")
public class ProductService {
    private final static long numberForSearchProductForArticle = 1_000_000_000L;
    private final ProductRepository productRepository;
    private final ProductKafkaProducer productKafkaProducer;
    private final ProductDtoFactory productDtoFactory;
    private final FeignClientInventoryService feignClientInventoryService;

    //возвращает только активные продукты
    public List<Product> findAllProducts() {
        return productRepository.findAllAvailableProducts();
    }

    //возвращает все продукты
    public List<Product> findAll() {
        return productRepository.findAll();
    }



    //создание продукта
    @Transactional
    public Product createProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty");}
        if(product.getPrice() < 0) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price cannot be negative");}
        if (product.getQuantity() < 0 /*|| product.getQuantity() == 0*/){throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity cannot be greater than zero");}
        if (product.getCategory() == null ||!product.getCategory().equals(Category.car)&&!product.getCategory().equals(Category.tech)&&!product.getCategory().equals(Category.tools)&&!product.getCategory().equals(Category.cloth)){throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The product category must be selected correctly.");}
        product.setArticle(generateArticle(product.getCategory()));
        if(product.getQuantity() > 0) {product.setAvailable(true);}
        productKafkaProducer.sendProductKafka(productDtoFactory.createProductRequestKafka(product));
        log.info("Product sent to kafka: article={}", product.getArticle());
        return productRepository.save(product);
    }

    //генерация артикула в зависимости от категории
    private long generateArticle(Category category) {
        long baseNumber = switch (category) {
            case car -> 1_000_000_000L;
            case tech -> 2_000_000_000L;
            case cloth -> 3_000_000_000L;
            case tools -> 4_000_000_000L;
        };

        Product lastProduct = productRepository.findTopByCategoryOrderByArticleDesc(category);

        return (lastProduct == null)
                ? baseNumber
                : lastProduct.getArticle() + 1;
    }

    //поиск продукта по артикулу
    @Cacheable(key = "#article")
    public Product findProductByArticle(long article) {
        long firstNumber = article / numberForSearchProductForArticle;
        Category findCategory = switch ((int) firstNumber){
            case 1 -> Category.car;
            case 2 -> Category.tech;
            case 3 -> Category.cloth;
            case 4 -> Category.tools;
            default -> throw new IllegalStateException("Unexpected value: " + (int) firstNumber);

        };
        try {
            Product product = productRepository.findProductByCategoryAndArticle(findCategory, article);
            if (product == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
            product.setQuantity(feignClientInventoryService.getInventoryQuantityByArticle(article));
            return product;
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

    }

    //поиск продуктов по категории
    @Cacheable(key = "{#category, #sort}")
    public List<Product> findAllProductsByCategory(Category category, Integer sort) {
        if (sort == null || sort < 0) {sort = 0;}
        if (sort == 1){
            return productRepository.findProductsByCategoryOrderByPriceAsc(category);
        }else if (sort == 2){
            return productRepository.findProductsByCategoryOrderByPriceDesc(category);
        }

        return productRepository.findProductsByCategory(category);
    }

    //добавить количество к продукту
    public Product addQuantityToProduct(long article, int quantity) {
        Product product = findProductByArticle(article);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        log.info("продукт найден с артикулом " + article);
        product.setQuantity(product.getQuantity() + quantity);
        log.info("продукту установленно количество = " + product.getQuantity());
        productKafkaProducer.sendProductKafka(productDtoFactory.createProductRequestKafka(product));
        return productRepository.save(product);
    }


}