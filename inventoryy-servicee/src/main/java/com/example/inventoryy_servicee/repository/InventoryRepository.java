package com.example.inventoryy_servicee.repository;

import com.example.inventoryy_servicee.model.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<ProductInventory, Long> {
    //проверка есть ли товар с данным артикулом
    boolean existsByArticle(long article);
    //поиск по артикулу
    ProductInventory findByArticle(long article);
    //удаление по артикулу
    void deleteByArticle(long article);

    //обновление количества по артикуду
    @Modifying
    @Transactional
    @Query("UPDATE ProductInventory p SET p.quantity = :quantity WHERE p.article = :article")
    int updateQuantity(@Param("article") long article, @Param("quantity") int quantity);


    //количество по артикулу
    @Query("SELECT pi.quantity FROM ProductInventory pi WHERE pi.article = :article")
    int productInventoryQuantityByArticle(long article);


}
