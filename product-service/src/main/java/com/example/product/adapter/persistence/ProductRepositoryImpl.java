package com.example.product.adapter.persistence;

import com.example.product.application.port.ProductRepository;
import com.example.product.domain.Product;
import com.example.product.domain.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    
    private final JpaProductRepository jpaRepository;
    private final ProductMapper mapper;
    
    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        return mapper.toDomain(jpaRepository.save(entity));
    }
    
    @Override
    public Optional<Product> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Product> findBySku(String sku) {
        return jpaRepository.findBySku(sku)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Product> findByCategory(String categoryId, int page, int size) {
        return jpaRepository.findByCategoryId(categoryId, PageRequest.of(page, size))
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByNameContainingAndStatus(String keyword, ProductStatus status, int page, int size) {
        return jpaRepository.findByNameContainingAndStatus(keyword, status, PageRequest.of(page, size))
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByStockQuantityLessThan(int threshold, int page, int size) {
        return jpaRepository.findByStockQuantityLessThan(threshold, PageRequest.of(page, size))
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
