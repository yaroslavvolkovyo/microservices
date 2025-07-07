package com.example.productt_servicee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProducttServiceeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducttServiceeApplication.class, args);
	}

}
