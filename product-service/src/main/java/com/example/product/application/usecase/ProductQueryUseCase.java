package com.example.product.application.usecase;

import com.example.product.domain.Product;
import com.example.product.application.query.ProductQueries.*;

import java.util.List;

public interface ProductQueryUseCase {
    Product getProduct(GetProductQuery query);
    List<Product> searchProducts(SearchProductsQuery query);
    List<Product> getProductsByCategory(GetProductsByCategoryQuery query);
    List<Product> getLowStockProducts(GetLowStockProductsQuery query);
}
