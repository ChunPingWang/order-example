package com.example.product.application.usecase;

import com.example.product.domain.Product;
import com.example.product.application.command.ProductCommands.*;

public interface ProductCommandUseCase {
    Product createProduct(CreateProductCommand command);
    Product updateProduct(UpdateProductCommand command);
    Product updateProductPrice(UpdateProductPriceCommand command);
    Product updateStockQuantity(UpdateStockQuantityCommand command);
    Product discontinueProduct(DiscontinueProductCommand command);
}
