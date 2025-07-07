package com.example.productt_servicee.repository;

import com.example.productt_servicee.dto.ProductShortResponse;
import com.example.productt_servicee.model.Category;
import com.example.productt_servicee.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    //@Query(value = "{}", fields = "{ 'article' : 1, 'name' : 1, 'price' : 1, 'category' : 1 }")

    Product findTopByCategoryOrderByArticleDesc(Category category);//возвращает артикул последнего добавленного товара
    @Query("{ 'available': true }")
    List<Product> findAllAvailableProducts();

    Product findProductByArticle(long article);

    Product findProductByCategoryAndArticle(Category category, long article);

    List<Product> findProductsByCategory(Category category);

    List<Product> findProductsByCategoryOrderByPriceAsc(Category category);

    List<Product> findProductsByCategoryOrderByPriceDesc(Category category);
}
