package com.example.product.adapter.web;

import com.example.product.application.command.ProductCommands.*;
import com.example.product.application.query.ProductQueries.*;
import com.example.product.application.usecase.ProductCommandUseCase;
import com.example.product.application.usecase.ProductQueryUseCase;
import com.example.product.domain.Product;
import com.example.product.domain.ProductStatus;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductCommandUseCase commandUseCase;
    private final ProductQueryUseCase queryUseCase;

    @PostMapping
    @Timed(value = "products.create", description = "Time taken to create new products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandUseCase.createProduct(command));
    }

    @PutMapping("/{id}")
    @Timed(value = "products.update", description = "Time taken to update products")
    public ResponseEntity<Product> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody UpdateProductCommand command) {
        return ResponseEntity.ok(commandUseCase.updateProduct(command));
    }

    @PutMapping("/{id}/price")
    @Timed(value = "products.updatePrice", description = "Time taken to update product price")
    public ResponseEntity<Product> updateProductPrice(
            @PathVariable String id,
            @Valid @RequestBody UpdateProductPriceCommand command) {
        return ResponseEntity.ok(commandUseCase.updateProductPrice(command));
    }

    @PutMapping("/{id}/stock")
    @Timed(value = "products.updateStock", description = "Time taken to update product stock")
    public ResponseEntity<Product> updateStockQuantity(
            @PathVariable String id,
            @Valid @RequestBody UpdateStockQuantityCommand command) {
        return ResponseEntity.ok(commandUseCase.updateStockQuantity(command));
    }

    @PostMapping("/{id}/discontinue")
    @Timed(value = "products.discontinue", description = "Time taken to discontinue products")
    public ResponseEntity<Product> discontinueProduct(@PathVariable String id) {
        return ResponseEntity.ok(
                commandUseCase.discontinueProduct(new DiscontinueProductCommand(id)));
    }

    @GetMapping("/{id}")
    @Timed(value = "products.get", description = "Time taken to retrieve products")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(
                queryUseCase.getProduct(new GetProductQuery(id)));
    }

    @GetMapping("/search")
    @Timed(value = "products.search", description = "Time taken to search products")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                queryUseCase.searchProducts(
                        new SearchProductsQuery(keyword, status, page, size)));
    }

    @GetMapping("/category/{categoryId}")
    @Timed(value = "products.getByCategory", description = "Time taken to get products by category")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                queryUseCase.getProductsByCategory(
                        new GetProductsByCategoryQuery(categoryId, page, size)));
    }

    @GetMapping("/low-stock")
    @Timed(value = "products.getLowStock", description = "Time taken to get low stock products")
    public ResponseEntity<List<Product>> getLowStockProducts(
            @RequestParam(defaultValue = "10") int threshold,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                queryUseCase.getLowStockProducts(
                        new GetLowStockProductsQuery(threshold, page, size)));
    }
}
