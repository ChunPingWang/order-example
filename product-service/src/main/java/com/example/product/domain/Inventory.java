package com.example.product.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory {
    private String productId;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private Integer alertThreshold;
    
    public Inventory(String productId, Integer quantity, Integer alertThreshold) {
        this.productId = productId;
        this.quantity = quantity;
        this.reservedQuantity = 0;
        this.availableQuantity = quantity;
        this.alertThreshold = alertThreshold;
    }
    
    public void increaseStock(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.quantity += amount;
        this.availableQuantity += amount;
    }
    
    public void decreaseStock(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.availableQuantity < amount) {
            throw new IllegalStateException("Insufficient stock");
        }
        this.quantity -= amount;
        this.availableQuantity -= amount;
    }
    
    public void reserveStock(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.availableQuantity < amount) {
            throw new IllegalStateException("Insufficient stock for reservation");
        }
        this.reservedQuantity += amount;
        this.availableQuantity -= amount;
    }
    
    public void cancelReservation(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("Invalid reservation cancellation amount");
        }
        this.reservedQuantity -= amount;
        this.availableQuantity += amount;
    }
    
    public void confirmReservation(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("Invalid reservation confirmation amount");
        }
        this.reservedQuantity -= amount;
        this.quantity -= amount;
    }
    
    public boolean isLowStock() {
        return this.availableQuantity <= this.alertThreshold;
    }
    
    public void updateAlertThreshold(Integer threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
        this.alertThreshold = threshold;
    }
}
