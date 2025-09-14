package com.example.product.application.port.in;

import java.util.List;

public interface InventoryUseCase {
    
    record IncreaseStockCommand(String productId, Integer amount, String reason, String operatedBy) {}
    record DecreaseStockCommand(String productId, Integer amount, String reason, String operatedBy) {}
    record ReserveStockCommand(String productId, Integer amount, String reason, String operatedBy) {}
    record CancelReservationCommand(String productId, Integer amount, String reason, String operatedBy) {}
    record ConfirmReservationCommand(String productId, Integer amount, String reason, String operatedBy) {}
    record UpdateAlertThresholdCommand(String productId, Integer threshold, String operatedBy) {}
    
    record InventoryResponse(
        String productId,
        Integer quantity,
        Integer reservedQuantity,
        Integer availableQuantity,
        Integer alertThreshold,
        boolean isLowStock
    ) {}
    
    record InventoryHistoryResponse(
        String productId,
        String operation,
        Integer quantity,
        Integer stockBefore,
        Integer stockAfter,
        String reason,
        String timestamp,
        String operatedBy
    ) {}
    
    void increaseStock(IncreaseStockCommand command);
    void decreaseStock(DecreaseStockCommand command);
    void reserveStock(ReserveStockCommand command);
    void cancelReservation(CancelReservationCommand command);
    void confirmReservation(ConfirmReservationCommand command);
    void updateAlertThreshold(UpdateAlertThresholdCommand command);
    
    InventoryResponse getInventory(String productId);
    List<InventoryResponse> getInventories(List<String> productIds);
    List<InventoryHistoryResponse> getInventoryHistory(String productId);
    List<InventoryHistoryResponse> getInventoryHistoryByOperation(String productId, List<String> operations);
}
