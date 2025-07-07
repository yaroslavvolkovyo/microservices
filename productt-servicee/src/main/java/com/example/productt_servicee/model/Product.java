package com.example.productt_servicee.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Product {
    @Id
    private String id;
    private long article;
    private String name;
    private int price;
    private boolean available;
    private int quantity;
    private Category category;

}
