package com.javatechie;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;

@SpringBootApplication
@EnableBinding(Processor.class)
public class DiscountServiceApplication {

	Logger logger = LoggerFactory.getLogger(DiscountServiceApplication.class);

	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public List<Product> addDiscountToProduct(List<Product> products) {
		List<Product> productList = new ArrayList<>();
		for (Product product : products) {
			if (product.getPrice() > 8000) {
				productList.add(calculatePrice(product, 10));
			} else if (product.getPrice() > 5000) {
				productList.add(calculatePrice(product, 5));
			}
		}
		return productList;
	}

	private Product calculatePrice(Product product, int percentage) {
		double actualPrice = product.getPrice();
		double discount = actualPrice * percentage / 100;
		product.setPrice(actualPrice - discount);
		logger.info("Product actual price is {} , after discount total price is {} ", actualPrice, product.getPrice());
		return product;
	}

	public static void main(String[] args) {
		SpringApplication.run(DiscountServiceApplication.class, args);
	}

}
