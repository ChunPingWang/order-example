package com.example.order.domain;

import com.example.order.domain.event.OrderEvent;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Order {
    private final String id;
    private final String customerId;
    private final List<OrderItem> items;
    private final Address shippingAddress;
    private OrderStatus status;
    private Money totalAmount;
    private Instant createdAt;
    private Instant updatedAt;
    private List<OrderEvent> domainEvents;

    public Order(String customerId, List<OrderItem> items, Address shippingAddress) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.shippingAddress = shippingAddress;
        this.status = OrderStatus.CREATED;
        this.totalAmount = calculateTotalAmount();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.domainEvents = new ArrayList<>();
    }

    private Money calculateTotalAmount() {
        return items.stream()
                .map(item -> Money.of(item.getSubtotal(), "USD"))
                .reduce(Money.of(java.math.BigDecimal.ZERO, "USD"), Money::add);
    }

    public void addItem(OrderItem item) {
        validateOrderEditable();
        items.add(item);
        totalAmount = calculateTotalAmount();
        updateTimestamp();
    }

    public void removeItem(String productId) {
        validateOrderEditable();
        items.removeIf(item -> item.getProductId().equals(productId));
        totalAmount = calculateTotalAmount();
        updateTimestamp();
    }

    public void updateItemQuantity(String productId, int newQuantity) {
        validateOrderEditable();
        items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    items.remove(item);
                    items.add(item.updateQuantity(newQuantity));
                    totalAmount = calculateTotalAmount();
                });
        updateTimestamp();
    }

    public void confirmOrder() {
        validateStatus(OrderStatus.CREATED);
        this.status = OrderStatus.CONFIRMED;
        updateTimestamp();
    }

    public void markAsPaid() {
        validateStatus(OrderStatus.CONFIRMED);
        this.status = OrderStatus.PAID;
        updateTimestamp();
    }

    public void ship() {
        validateStatus(OrderStatus.PAID);
        this.status = OrderStatus.SHIPPING;
        updateTimestamp();
    }

    public void deliver() {
        validateStatus(OrderStatus.SHIPPING);
        this.status = OrderStatus.DELIVERED;
        updateTimestamp();
    }

    public void cancel(String reason) {
        validateOrderCancellable();
        this.status = OrderStatus.CANCELLED;
        updateTimestamp();
    }

    private void validateOrderEditable() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Order can only be edited when in CREATED status");
        }
    }

    private void validateOrderCancellable() {
        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order cannot be cancelled in current status: " + status);
        }
    }

    private void validateStatus(OrderStatus expectedStatus) {
        if (status != expectedStatus) {
            throw new IllegalStateException(
                    String.format("Invalid order status. Expected: %s, Current: %s",
                            expectedStatus, status));
        }
    }

    private void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    public List<OrderEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    protected void addDomainEvent(OrderEvent event) {
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
