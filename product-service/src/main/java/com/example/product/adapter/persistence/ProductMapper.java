package com.example.product.adapter.persistence;

import com.example.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    
    public Product toDomain(ProductEntity entity) {
        return new Product(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            entity.getStockQuantity(),
            entity.getStatus(),
            entity.getCategoryId(),
            entity.getSku()
        );
    }
    
    public ProductEntity toEntity(Product product) {
        return new ProductEntity(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getStatus(),
            product.getCategoryId(),
            product.getSku(),
            null  // version
        );
    }
}
