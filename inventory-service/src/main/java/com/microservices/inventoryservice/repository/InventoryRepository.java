package com.microservices.inventoryservice.repository;

import com.microservices.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findBySKUCode(String skuCode);

    Optional<List<Inventory>> findBySKUCodeIn(List<String> skuCodes);
}
