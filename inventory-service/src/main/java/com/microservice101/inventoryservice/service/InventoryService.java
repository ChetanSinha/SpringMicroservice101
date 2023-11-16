package com.microservice101.inventoryservice.service;

import com.microservice101.inventoryservice.dto.InventoryRequest;
import com.microservice101.inventoryservice.model.Inventory;
import com.microservice101.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
