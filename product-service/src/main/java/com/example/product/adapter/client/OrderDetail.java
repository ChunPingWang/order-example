package com.example.product.adapter.client;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetail {
    private String orderId;
    private String productId;
    private Integer quantity;
    private BigDecimal price;
    private String status;
    private LocalDateTime orderDate;
}
