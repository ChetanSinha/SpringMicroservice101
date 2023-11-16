package com.microservice101.inventoryservice.controller;

import com.microservice101.inventoryservice.dto.InventoryRequest;
import com.microservice101.inventoryservice.model.Inventory;
import com.microservice101.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<String> addInventory(@RequestBody InventoryRequest inventoryRequest) {
        inventoryService.createInventory(inventoryRequest);
        return ResponseEntity.ok("Inventory added");
    }

    @GetMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<Boolean> isStockAvailable(@PathVariable String skuCode) {
        boolean stockAvailable = inventoryService.isStockAvailable(skuCode);
        return ResponseEntity.ok(stockAvailable);
    }
}
