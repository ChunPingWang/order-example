package com.example.product.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InventoryLowStockEvent {
    private final String productId;
    private final Integer currentStock;
    private final Integer threshold;
    private final LocalDateTime timestamp;

    public InventoryLowStockEvent(String productId, Integer currentStock, Integer threshold) {
        this.productId = productId;
        this.currentStock = currentStock;
        this.threshold = threshold;
        this.timestamp = LocalDateTime.now();
    }
}
