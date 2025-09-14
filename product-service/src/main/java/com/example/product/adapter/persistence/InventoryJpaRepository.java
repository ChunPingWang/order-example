package com.example.product.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface InventoryJpaRepository extends JpaRepository<InventoryJpaEntity, Long> {
    Optional<InventoryJpaEntity> findByProductId(String productId);
    List<InventoryJpaEntity> findAllByProductIdIn(List<String> productIds);
    void deleteByProductId(String productId);
}

@Repository
interface InventoryHistoryJpaRepository extends JpaRepository<InventoryHistoryJpaEntity, Long> {
    List<InventoryHistoryJpaEntity> findByProductId(String productId);
    List<InventoryHistoryJpaEntity> findByProductIdAndOperationIn(String productId, List<InventoryOperation> operations);
}
