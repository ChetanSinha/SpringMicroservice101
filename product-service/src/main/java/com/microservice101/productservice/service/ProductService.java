package com.microservice101.productservice.service;

import com.microservice101.productservice.dto.ProductRequest;
import com.microservice101.productservice.dto.ProductResponse;
import com.microservice101.productservice.model.Product;
import com.microservice101.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product created.... with id:{}", product.getId());
    }

    public List<ProductResponse> getAll() {
        List<Product> allProduct = productRepository.findAll();
        return allProduct.stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
