package com.example.order.domain.service;

import com.example.order.domain.Order;
import com.example.order.domain.OrderItem;
import com.example.order.domain.Address;

import java.util.List;

public class OrderDomainService {
    
    public Order createOrder(String customerId, List<OrderItem> items, Address shippingAddress) {
        validateOrderItems(items);
        return new Order(customerId, items, shippingAddress);
    }
    
    private void validateOrderItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        
        items.forEach(item -> {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item quantity must be greater than zero");
            }
            if (item.getUnitPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Item unit price must be greater than zero");
            }
        });
    }
}
