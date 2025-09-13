package com.example.product.application.query;

import lombok.Value;
import com.example.product.domain.ProductStatus;

public class ProductQueries {
    
    @Value
    public static class GetProductQuery {
        String id;
    }
    
    @Value
    public static class SearchProductsQuery {
        String keyword;
        ProductStatus status;
        String categoryId;
        int page;
        int size;
    }
    
    @Value
    public static class GetProductsByCategoryQuery {
        String categoryId;
        int page;
        int size;
    }
    
    @Value
    public static class GetLowStockProductsQuery {
        int threshold;
        int page;
        int size;
    }
}
