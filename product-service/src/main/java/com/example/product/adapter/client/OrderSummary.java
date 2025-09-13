package com.example.product.adapter.client;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderSummary {
    private String orderId;
    private String customerId;
    private List<OrderDetail> orderDetails;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
}
