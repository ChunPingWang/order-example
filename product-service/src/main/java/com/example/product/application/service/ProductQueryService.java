package com.example.product.application.service;

import com.example.product.application.port.ProductRepository;
import com.example.product.application.query.ProductQueries.*;
import com.example.product.application.usecase.ProductQueryUseCase;
import com.example.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQueryService implements ProductQueryUseCase {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Product getProduct(GetProductQuery query) {
        return productRepository.findById(query.getId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProducts(SearchProductsQuery query) {
        return productRepository.findByNameContainingAndStatus(
            query.getKeyword(),
            query.getStatus(),
            query.getPage(),
            query.getSize()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(GetProductsByCategoryQuery query) {
        return productRepository.findByCategory(
            query.getCategoryId(),
            query.getPage(),
            query.getSize()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts(GetLowStockProductsQuery query) {
        return productRepository.findByStockQuantityLessThan(
            query.getThreshold(),
            query.getPage(),
            query.getSize()
        );
    }
}
