package com.microservices.inventoryservice.controller;

import com.microservices.inventoryservice.dto.InventoryRequest;
import com.microservices.inventoryservice.dto.InventoryResponse;
import com.microservices.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<List<InventoryResponse>> isStockAvailableMulti(@RequestParam("skuCodes") List<String> skuCodes) {
        List<InventoryResponse> responses = inventoryService.isStockAvailableMulti(skuCodes);
        return ResponseEntity.ok(responses);
    }
}
