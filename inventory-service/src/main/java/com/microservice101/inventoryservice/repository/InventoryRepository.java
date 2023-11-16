package com.microservice101.inventoryservice.repository;

import com.microservice101.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findBySKUCode(String skuCode);
}
