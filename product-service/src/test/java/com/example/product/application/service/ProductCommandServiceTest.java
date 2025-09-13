package com.example.product.application.service;

import com.example.product.application.command.ProductCommands.*;
import com.example.product.application.port.ProductRepository;
import com.example.product.domain.Product;
import com.example.product.domain.ProductStatus;
import com.example.product.domain.event.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductCommandServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    private ProductCommandService service;

    @BeforeEach
    void setUp() {
        service = new ProductCommandService(productRepository, eventPublisher);
    }

    @Test
    void whenCreateProduct_thenSuccess() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            "Test Product",
            "Description",
            new BigDecimal("99.99"),
            100,
            "category-1",
            "TEST-SKU"
        );

        Product expectedProduct = new Product(
            "1",
            command.getName(),
            command.getDescription(),
            command.getPrice(),
            command.getStockQuantity(),
            ProductStatus.IN_STOCK,
            command.getCategoryId(),
            command.getSku()
        );

        when(productRepository.findBySku(command.getSku())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        // Act
        Product result = service.createProduct(command);

        // Assert
        assertNotNull(result);
        assertEquals(expectedProduct.getName(), result.getName());
        assertEquals(expectedProduct.getSku(), result.getSku());
        verify(eventPublisher).publish(any());
    }

    @Test
    void whenCreateProductWithDuplicateSku_thenThrowException() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            "Test Product",
            "Description",
            new BigDecimal("99.99"),
            100,
            "category-1",
            "EXISTING-SKU"
        );

        when(productRepository.findBySku(command.getSku()))
            .thenReturn(Optional.of(new Product()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> service.createProduct(command));
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void whenUpdateStock_thenSuccess() {
        // Arrange
        String productId = "1";
        UpdateStockQuantityCommand command = new UpdateStockQuantityCommand(productId, -10);
        
        Product existingProduct = new Product(
            productId,
            "Test Product",
            "Description",
            new BigDecimal("99.99"),
            100,
            ProductStatus.IN_STOCK,
            "category-1",
            "TEST-SKU"
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Product result = service.updateStockQuantity(command);

        // Assert
        assertEquals(90, result.getStockQuantity());
        verify(eventPublisher).publish(any());
    }

    @Test
    void whenUpdateStockBelowZero_thenThrowException() {
        // Arrange
        String productId = "1";
        UpdateStockQuantityCommand command = new UpdateStockQuantityCommand(productId, -110);
        
        Product existingProduct = new Product(
            productId,
            "Test Product",
            "Description",
            new BigDecimal("99.99"),
            100,
            ProductStatus.IN_STOCK,
            "category-1",
            "TEST-SKU"
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> service.updateStockQuantity(command));
        verify(eventPublisher, never()).publish(any());
    }
}
