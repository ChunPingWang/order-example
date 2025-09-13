package com.example.product.adapter.persistence;

import com.example.product.domain.ProductStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, String> {
    Optional<ProductEntity> findBySku(String sku);
    List<ProductEntity> findByCategoryId(String categoryId, Pageable pageable);
    List<ProductEntity> findByNameContainingAndStatus(String name, ProductStatus status, Pageable pageable);
    List<ProductEntity> findByStockQuantityLessThan(int threshold, Pageable pageable);
}
