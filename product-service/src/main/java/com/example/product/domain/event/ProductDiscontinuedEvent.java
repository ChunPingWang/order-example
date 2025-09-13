package com.example.product.domain.event;

import com.example.product.domain.Product;
import lombok.Getter;

@Getter
public class ProductDiscontinuedEvent extends DomainEvent {
    private final Product product;
    
    public ProductDiscontinuedEvent(Product product) {
        this.product = product;
    }
}
