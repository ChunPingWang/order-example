package com.example.product.application.service;

import com.example.product.application.command.ProductCommands.*;
import com.example.product.application.port.ProductRepository;
import com.example.product.application.usecase.ProductCommandUseCase;
import com.example.product.domain.Product;
import com.example.product.domain.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCommandService implements ProductCommandUseCase {

    private final ProductRepository productRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public Product createProduct(CreateProductCommand command) {
        // 檢查 SKU 是否已存在
        if (productRepository.findBySku(command.getSku()).isPresent()) {
            throw new IllegalArgumentException("Product with SKU " + command.getSku() + " already exists");
        }

        Product product = new Product(
            null,
            command.getName(),
            command.getDescription(),
            command.getPrice(),
            command.getStockQuantity(),
            ProductStatus.IN_STOCK,
            command.getCategoryId(),
            command.getSku()
        );

        Product savedProduct = productRepository.save(product);
        eventPublisher.publish(new ProductCreatedEvent(savedProduct));
        return savedProduct;
    }

    @Override
    @Transactional
    public Product updateProduct(UpdateProductCommand command) {
        Product product = productRepository.findById(command.getId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Product updatedProduct = new Product(
            product.getId(),
            command.getName(),
            command.getDescription(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getStatus(),
            command.getCategoryId(),
            product.getSku()
        );

        return productRepository.save(updatedProduct);
    }

    @Override
    @Transactional
    public Product updateProductPrice(UpdateProductPriceCommand command) {
        Product product = productRepository.findById(command.getId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.updatePrice(command.getPrice());
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateStockQuantity(UpdateStockQuantityCommand command) {
        Product product = productRepository.findById(command.getId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.updateStock(command.getQuantityChange());
        Product updatedProduct = productRepository.save(product);
        eventPublisher.publish(new ProductStockUpdatedEvent(updatedProduct, command.getQuantityChange()));
        return updatedProduct;
    }

    @Override
    @Transactional
    public Product discontinueProduct(DiscontinueProductCommand command) {
        Product product = productRepository.findById(command.getId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Product discontinuedProduct = new Product(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            0,
            ProductStatus.DISCONTINUED,
            product.getCategoryId(),
            product.getSku()
        );

        Product savedProduct = productRepository.save(discontinuedProduct);
        eventPublisher.publish(new ProductDiscontinuedEvent(savedProduct));
        return savedProduct;
    }
}
