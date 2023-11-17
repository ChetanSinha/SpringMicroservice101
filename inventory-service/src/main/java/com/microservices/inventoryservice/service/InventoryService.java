package com.microservices.inventoryservice.service;

import com.microservices.inventoryservice.dto.InventoryRequest;
import com.microservices.inventoryservice.dto.InventoryResponse;
import com.microservices.inventoryservice.model.Inventory;
import com.microservices.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    public void createInventory(InventoryRequest inventoryRequest) {
        inventoryRepository.save(Inventory.builder()
                .SKUCode(inventoryRequest.getSKUCode())
                .quantity(inventoryRequest.getQuantity())
                .build());
    }

    public boolean isStockAvailable(String skuCode) {
        Inventory inventory = inventoryRepository.findBySKUCode(skuCode).orElse(null);
        return inventory != null && inventory.getQuantity() > 0;
    }

    public List<InventoryResponse> isStockAvailableMulti(List<String> skuCodes) {
        List<Inventory> inventories = inventoryRepository.findBySKUCodeIn(skuCodes).orElse(new ArrayList<>());

        Map<String, Integer> skuCodeQuantityMap = inventories.stream()
                .collect(Collectors.toMap(Inventory::getSKUCode, Inventory::getQuantity));

        return skuCodes.stream()
                .map(skuCode -> InventoryResponse.builder()
                        .skuCode(skuCode)
                        .inStock(skuCodeQuantityMap.containsKey(skuCode) && skuCodeQuantityMap.get(skuCode) > 0)
                        .build())
                .collect(Collectors.toList());
    }
}
