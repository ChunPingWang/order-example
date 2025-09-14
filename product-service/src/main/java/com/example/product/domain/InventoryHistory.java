package com.example.product.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryHistory {
    private String id;
    private String productId;
    private InventoryOperation operation;
    private Integer quantity;
    private Integer stockBefore;
    private Integer stockAfter;
    private String reason;
    private LocalDateTime timestamp;
    private String operatedBy;
    
    public InventoryHistory(
            String productId,
            InventoryOperation operation,
            Integer quantity,
            Integer stockBefore,
            Integer stockAfter,
            String reason,
            String operatedBy
    ) {
        this.productId = productId;
        this.operation = operation;
        this.quantity = quantity;
        this.stockBefore = stockBefore;
        this.stockAfter = stockAfter;
        this.reason = reason;
        this.operatedBy = operatedBy;
        this.timestamp = LocalDateTime.now();
    }
}

public enum InventoryOperation {
    INCREASE,
    DECREASE,
    RESERVE,
    CANCEL_RESERVATION,
    CONFIRM_RESERVATION,
    ADJUST
}
