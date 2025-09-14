package com.example.product.adapter.persistence;

import com.example.product.application.port.out.InventoryRepository;
import com.example.product.domain.Inventory;
import com.example.product.domain.InventoryHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class InventoryPersistenceAdapter implements InventoryRepository, InventoryHistoryRepository {
    
    private final InventoryJpaRepository inventoryJpaRepository;
    private final InventoryHistoryJpaRepository inventoryHistoryJpaRepository;
    
    @Override
    public Optional<Inventory> findByProductId(String productId) {
        return inventoryJpaRepository.findByProductId(productId)
                .map(this::mapToDomainEntity);
    }
    
    @Override
    public Inventory save(Inventory inventory) {
        InventoryJpaEntity entity = mapToJpaEntity(inventory);
        entity = inventoryJpaRepository.save(entity);
        return mapToDomainEntity(entity);
    }
    
    @Override
    public List<Inventory> findAllByProductIdIn(List<String> productIds) {
        return inventoryJpaRepository.findAllByProductIdIn(productIds)
                .stream()
                .map(this::mapToDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteByProductId(String productId) {
        inventoryJpaRepository.deleteByProductId(productId);
    }
    
    @Override
    public InventoryHistory save(InventoryHistory history) {
        InventoryHistoryJpaEntity entity = mapToJpaEntity(history);
        entity = inventoryHistoryJpaRepository.save(entity);
        return mapToDomainEntity(entity);
    }
    
    @Override
    public List<InventoryHistory> findByProductId(String productId) {
        return inventoryHistoryJpaRepository.findByProductId(productId)
                .stream()
                .map(this::mapToDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<InventoryHistory> findByProductIdAndOperationIn(String productId, List<String> operations) {
        List<InventoryOperation> ops = operations.stream()
                .map(InventoryOperation::valueOf)
                .collect(Collectors.toList());
                
        return inventoryHistoryJpaRepository.findByProductIdAndOperationIn(productId, ops)
                .stream()
                .map(this::mapToDomainEntity)
                .collect(Collectors.toList());
    }
    
    private Inventory mapToDomainEntity(InventoryJpaEntity entity) {
        Inventory inventory = new Inventory(
                entity.getProductId(),
                entity.getQuantity(),
                entity.getAlertThreshold()
        );
        // 手動設置其他屬性，因為這些在建構子中沒有設置
        inventory.reserveStock(entity.getReservedQuantity());
        return inventory;
    }
    
    private InventoryJpaEntity mapToJpaEntity(Inventory inventory) {
        InventoryJpaEntity entity = new InventoryJpaEntity();
        entity.setProductId(inventory.getProductId());
        entity.setQuantity(inventory.getQuantity());
        entity.setReservedQuantity(inventory.getReservedQuantity());
        entity.setAvailableQuantity(inventory.getAvailableQuantity());
        entity.setAlertThreshold(inventory.getAlertThreshold());
        return entity;
    }
    
    private InventoryHistory mapToDomainEntity(InventoryHistoryJpaEntity entity) {
        return new InventoryHistory(
                entity.getProductId(),
                entity.getOperation(),
                entity.getQuantity(),
                entity.getStockBefore(),
                entity.getStockAfter(),
                entity.getReason(),
                entity.getOperatedBy()
        );
    }
    
    private InventoryHistoryJpaEntity mapToJpaEntity(InventoryHistory history) {
        InventoryHistoryJpaEntity entity = new InventoryHistoryJpaEntity();
        entity.setProductId(history.getProductId());
        entity.setOperation(InventoryOperation.valueOf(history.getOperation().name()));
        entity.setQuantity(history.getQuantity());
        entity.setStockBefore(history.getStockBefore());
        entity.setStockAfter(history.getStockAfter());
        entity.setReason(history.getReason());
        entity.setTimestamp(history.getTimestamp());
        entity.setOperatedBy(history.getOperatedBy());
        return entity;
    }
}
