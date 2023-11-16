package com.microservice101.orderservice.controller;

import com.microservice101.orderservice.dto.OrderRequest;
import com.microservice101.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            orderService.placeOrder(orderRequest);
            return ResponseEntity.ok("Order placed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
}
