package com.example.productt_servicee.dto;

import com.example.productt_servicee.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailsResponse{
    private long article;
    private String name;
    private int price;
    private Category category;
    private int quantity;
}
