package com.example.order.domain;

import lombok.Value;
import java.math.BigDecimal;

@Value
public class OrderItem {
    String productId;
    String productName;
    int quantity;
    BigDecimal unitPrice;
    BigDecimal subtotal;

    public OrderItem(String productId, String productName, int quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public OrderItem updateQuantity(int newQuantity) {
        return new OrderItem(this.productId, this.productName, newQuantity, this.unitPrice);
    }
}
