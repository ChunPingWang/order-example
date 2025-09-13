package com.example.product.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private ProductStatus status;
    private String categoryId;
    private String sku;
    
    public void updateStock(int quantity) {
        if (this.stockQuantity + quantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.stockQuantity += quantity;
        updateStatus();
    }
    
    public void updatePrice(BigDecimal newPrice) {
        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        this.price = newPrice;
    }
    
    private void updateStatus() {
        if (this.stockQuantity == 0) {
            this.status = ProductStatus.OUT_OF_STOCK;
        } else if (this.stockQuantity < 10) {
            this.status = ProductStatus.LOW_STOCK;
        } else {
            this.status = ProductStatus.IN_STOCK;
        }
    }
}
