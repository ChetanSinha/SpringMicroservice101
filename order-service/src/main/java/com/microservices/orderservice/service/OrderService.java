package com.microservices.orderservice.service;

import com.microservices.orderservice.config.WebClientConfig;
import com.microservices.orderservice.dto.InventoryResponse;
import com.microservices.orderservice.dto.OrderRequest;
import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.model.OrderLineItems;
import com.microservices.orderservice.repository.OrderRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WebClientConfig webClientConfig;

    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(orderItem -> OrderLineItems.builder()
                        .price(orderItem.getPrice())
                        .quantity(orderItem.getQuantity())
                        .SKUCode(orderItem.getSKUCode())
                        .build()
                )
                .collect(Collectors.toList());

        checkOrderStock(orderLineItems.stream()
                .map(OrderLineItems::getSKUCode)
                .toList());

        Order order = Order.builder()
                .orderNum(UUID.randomUUID().toString())
                .orderLineItemsList(orderLineItems)
                .build();

        orderRepository.save(order);
    }

//    Using discovery server with client load balanced request
    public void checkOrderStock(List<String> skuCodes) {
        InventoryResponse[] result = webClientConfig.webClient().build().get()
                .uri("http://inventory-service/api/inventory?skuCodes=" + Strings.join(skuCodes, ','))
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        if (result != null) {
            boolean inStock = Arrays.stream(result).allMatch(InventoryResponse::getInStock);
            if (inStock) {
                return;
            }
        }
        throw new IllegalArgumentException("Requested order is out of stock!");
    }
}
