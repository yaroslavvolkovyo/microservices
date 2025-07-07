package com.example.productt_servicee.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "inventoryy-servicee",url = "http://localhost:8081")

public interface FeignClientInventoryService {

    @GetMapping("/v1/inventory/{article}")
    public int getInventoryQuantityByArticle(@PathVariable("article") long article);
}
