package com.example.order.adapter.persistence;

import com.example.order.domain.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {
    
    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setCustomerId(order.getCustomerId());
        entity.setStatus(order.getStatus());
        entity.setTotalAmount(order.getTotalAmount().getAmount());
        entity.setCurrency(order.getTotalAmount().getCurrency().getCurrencyCode());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        
        entity.setItems(order.getItems().stream()
                .map(this::toItemEntity)
                .collect(Collectors.toList()));
        
        entity.setShippingAddress(toAddressEntity(order.getShippingAddress()));
        
        return entity;
    }
    
    public Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(this::toOrderItem)
                .collect(Collectors.toList());
                
        Address address = toDomainAddress(entity.getShippingAddress());
        
        Order order = new Order(entity.getCustomerId(), items, address);
        // 設置已保存實體的狀態
        order.setStatus(entity.getStatus());
        return order;
    }
    
    private OrderItemEntity toItemEntity(OrderItem item) {
        return new OrderItemEntity(
                null,
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
    
    private OrderItem toOrderItem(OrderItemEntity entity) {
        return new OrderItem(
                entity.getProductId(),
                entity.getProductName(),
                entity.getQuantity(),
                entity.getUnitPrice()
        );
    }
    
    private AddressEntity toAddressEntity(Address address) {
        return new AddressEntity(
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode(),
                address.getRecipientName(),
                address.getPhoneNumber()
        );
    }
    
    private Address toDomainAddress(AddressEntity entity) {
        return new Address(
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                entity.getPostalCode(),
                entity.getRecipientName(),
                entity.getPhoneNumber()
        );
    }
}
