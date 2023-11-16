package com.microservice101.orderservice.service;

import com.microservice101.orderservice.config.WebClientConfig;
import com.microservice101.orderservice.dto.InventoryResponse;
import com.microservice101.orderservice.dto.OrderRequest;
import com.microservice101.orderservice.model.Order;
import com.microservice101.orderservice.model.OrderLineItems;
import com.microservice101.orderservice.repository.OrderRepository;
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

    public void checkOrderStock(List<String> skuCodes) {
        InventoryResponse[] result = webClientConfig.webClient().get()
                .uri("http://localhost:8082/api/inventory?skuCodes=" + Strings.join(skuCodes, ','))
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
