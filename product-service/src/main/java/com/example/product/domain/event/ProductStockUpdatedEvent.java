package com.example.product.domain.event;

import com.example.product.domain.Product;
import lombok.Getter;

@Getter
public class ProductStockUpdatedEvent extends DomainEvent {
    private final Product product;
    private final int quantityChange;
    
    public ProductStockUpdatedEvent(Product product, int quantityChange) {
        this.product = product;
        this.quantityChange = quantityChange;
    }
}
