package com.example.product.application.service;

import com.example.product.application.port.in.InventoryUseCase;
import com.example.product.application.port.out.InventoryRepository;
import com.example.product.domain.Inventory;
import com.example.product.domain.InventoryHistory;
import com.example.product.domain.event.InventoryLowStockEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService implements InventoryUseCase {
    
    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final InventoryMetrics metrics;
    
    @Override
    @CacheEvict(value = "inventory", key = "#command.productId()")
    public void increaseStock(IncreaseStockCommand command) {
        long startTime = System.currentTimeMillis();
        Inventory inventory = getInventoryOrThrow(command.productId());
        int stockBefore = inventory.getQuantity();
        
        inventory.increaseStock(command.amount());
        inventoryRepository.save(inventory);
        
        saveHistory(
            command.productId(),
            InventoryOperation.INCREASE,
            command.amount(),
            stockBefore,
            inventory.getQuantity(),
            command.reason(),
            command.operatedBy()
        );
        
        checkAndPublishLowStockEvent(inventory);
    }
    
    @Override
    public void decreaseStock(DecreaseStockCommand command) {
        Inventory inventory = getInventoryOrThrow(command.productId());
        int stockBefore = inventory.getQuantity();
        
        inventory.decreaseStock(command.amount());
        inventoryRepository.save(inventory);
        
        saveHistory(
            command.productId(),
            InventoryOperation.DECREASE,
            command.amount(),
            stockBefore,
            inventory.getQuantity(),
            command.reason(),
            command.operatedBy()
        );
        
        checkAndPublishLowStockEvent(inventory);
    }
    
    @Override
    public void reserveStock(ReserveStockCommand command) {
        Inventory inventory = getInventoryOrThrow(command.productId());
        int stockBefore = inventory.getAvailableQuantity();
        
        inventory.reserveStock(command.amount());
        inventoryRepository.save(inventory);
        
        saveHistory(
            command.productId(),
            InventoryOperation.RESERVE,
            command.amount(),
            stockBefore,
            inventory.getAvailableQuantity(),
            command.reason(),
            command.operatedBy()
        );
    }
    
    @Override
    public void cancelReservation(CancelReservationCommand command) {
        Inventory inventory = getInventoryOrThrow(command.productId());
        int stockBefore = inventory.getAvailableQuantity();
        
        inventory.cancelReservation(command.amount());
        inventoryRepository.save(inventory);
        
        saveHistory(
            command.productId(),
            InventoryOperation.CANCEL_RESERVATION,
            command.amount(),
            stockBefore,
            inventory.getAvailableQuantity(),
            command.reason(),
            command.operatedBy()
        );
    }
    
    @Override
    public void confirmReservation(ConfirmReservationCommand command) {
        Inventory inventory = getInventoryOrThrow(command.productId());
        int stockBefore = inventory.getQuantity();
        
        inventory.confirmReservation(command.amount());
        inventoryRepository.save(inventory);
        
        saveHistory(
            command.productId(),
            InventoryOperation.CONFIRM_RESERVATION,
            command.amount(),
            stockBefore,
            inventory.getQuantity(),
            command.reason(),
            command.operatedBy()
        );
        
        checkAndPublishLowStockEvent(inventory);
    }
    
    @Override
    public void updateAlertThreshold(UpdateAlertThresholdCommand command) {
        Inventory inventory = getInventoryOrThrow(command.productId());
        inventory.updateAlertThreshold(command.threshold());
        inventoryRepository.save(inventory);
        
        checkAndPublishLowStockEvent(inventory);
    }
    
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "inventory", key = "#productId")
    public InventoryResponse getInventory(String productId) {
        Inventory inventory = getInventoryOrThrow(productId);
        return mapToResponse(inventory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getInventories(List<String> productIds) {
        return inventoryRepository.findAllByProductIdIn(productIds)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryHistoryResponse> getInventoryHistory(String productId) {
        return inventoryHistoryRepository.findByProductId(productId)
                .stream()
                .map(this::mapToHistoryResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryHistoryResponse> getInventoryHistoryByOperation(String productId, List<String> operations) {
        return inventoryHistoryRepository.findByProductIdAndOperationIn(productId, operations)
                .stream()
                .map(this::mapToHistoryResponse)
                .collect(Collectors.toList());
    }
    
    private Inventory getInventoryOrThrow(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalStateException("Inventory not found for product: " + productId));
    }
    
    private void saveHistory(
            String productId,
            InventoryOperation operation,
            Integer quantity,
            Integer stockBefore,
            Integer stockAfter,
            String reason,
            String operatedBy
    ) {
        InventoryHistory history = new InventoryHistory(
                productId,
                operation,
                quantity,
                stockBefore,
                stockAfter,
                reason,
                operatedBy
        );
        inventoryHistoryRepository.save(history);
    }
    
    private void checkAndPublishLowStockEvent(Inventory inventory) {
        if (inventory.isLowStock()) {
            eventPublisher.publishEvent(new InventoryLowStockEvent(
                    inventory.getProductId(),
                    inventory.getAvailableQuantity(),
                    inventory.getAlertThreshold()
            ));
        }
    }
    
    private InventoryResponse mapToResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getProductId(),
                inventory.getQuantity(),
                inventory.getReservedQuantity(),
                inventory.getAvailableQuantity(),
                inventory.getAlertThreshold(),
                inventory.isLowStock()
        );
    }
    
    private InventoryHistoryResponse mapToHistoryResponse(InventoryHistory history) {
        return new InventoryHistoryResponse(
                history.getProductId(),
                history.getOperation().name(),
                history.getQuantity(),
                history.getStockBefore(),
                history.getStockAfter(),
                history.getReason(),
                history.getTimestamp().toString(),
                history.getOperatedBy()
        );
    }
}
