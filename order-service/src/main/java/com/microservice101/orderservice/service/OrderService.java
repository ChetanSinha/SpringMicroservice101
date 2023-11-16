package com.microservice101.orderservice.service;

import com.microservice101.orderservice.dto.OrderRequest;
import com.microservice101.orderservice.model.Order;
import com.microservice101.orderservice.model.OrderLineItems;
import com.microservice101.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(orderItem -> OrderLineItems.builder()
                        .price(orderItem.getPrice())
                        .quantity(orderItem.getQuantity())
                        .SKUCode(orderItem.getSKUCode())
                        .build()
                )
                .collect(Collectors.toList());
        Order order = Order.builder()
                .orderNum(UUID.randomUUID().toString())
                .orderLineItemsList(orderLineItems)
                .build();

        orderRepository.save(order);
    }
}
