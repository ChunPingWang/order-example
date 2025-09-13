package com.example.product.application.command;

import lombok.Value;
import java.math.BigDecimal;

public class ProductCommands {
    
    @Value
    public static class CreateProductCommand {
        String name;
        String description;
        BigDecimal price;
        Integer stockQuantity;
        String categoryId;
        String sku;
    }
    
    @Value
    public static class UpdateProductCommand {
        String id;
        String name;
        String description;
        String categoryId;
    }
    
    @Value
    public static class UpdateProductPriceCommand {
        String id;
        BigDecimal price;
    }
    
    @Value
    public static class UpdateStockQuantityCommand {
        String id;
        int quantityChange;
    }
    
    @Value
    public static class DiscontinueProductCommand {
        String id;
    }
}
