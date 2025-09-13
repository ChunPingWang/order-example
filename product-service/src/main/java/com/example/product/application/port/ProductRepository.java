package com.example.product.application.port;

import com.example.product.domain.Product;
import com.example.product.domain.ProductStatus;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(String id);
    Optional<Product> findBySku(String sku);
    List<Product> findByCategory(String categoryId, int page, int size);
    List<Product> findByNameContainingAndStatus(String keyword, ProductStatus status, int page, int size);
    List<Product> findByStockQuantityLessThan(int threshold, int page, int size);
    void deleteById(String id);
}
