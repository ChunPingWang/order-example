package com.example.product.domain.event;

import com.example.product.domain.Product;
import lombok.Getter;

@Getter
public class ProductCreatedEvent extends DomainEvent {
    private final Product product;
    
    public ProductCreatedEvent(Product product) {
        this.product = product;
    }
}
