package com.example.product.adapter.client;

import lombok.Data;
import java.math.BigDecimal;

public class OrderServiceClient {
    @Data
    public static class OrderDetail {
        private String orderId;
        private String productId;
        private int quantity;
        private BigDecimal price;
        private String status;
    }
    
    @Data
    public static class OrderSummary {
        private String orderId;
        private String customerId;
        private BigDecimal totalAmount;
        private String status;
    }
}
