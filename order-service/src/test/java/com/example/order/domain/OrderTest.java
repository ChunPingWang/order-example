package com.example.order.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    
    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        String customerId = "customer-123";
        OrderItem item = new OrderItem(
                "product-1",
                "Product One",
                2,
                BigDecimal.valueOf(100));
        Address address = new Address(
                "123 Street",
                "City",
                "State",
                "Country",
                "12345",
                "John Doe",
                "123-456-7890");

        // Act
        Order order = new Order(customerId, Arrays.asList(item), address);

        // Assert
        assertNotNull(order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(1, order.getItems().size());
        assertEquals(Money.of(BigDecimal.valueOf(200), "USD"), order.getTotalAmount());
    }

    @Test
    void shouldCalculateTotalAmountCorrectly() {
        // Arrange
        OrderItem item1 = new OrderItem(
                "product-1",
                "Product One",
                2,
                BigDecimal.valueOf(100));
        OrderItem item2 = new OrderItem(
                "product-2",
                "Product Two",
                1,
                BigDecimal.valueOf(50));
        Address address = new Address(
                "123 Street",
                "City",
                "State",
                "Country",
                "12345",
                "John Doe",
                "123-456-7890");

        // Act
        Order order = new Order("customer-123", Arrays.asList(item1, item2), address);

        // Assert
        assertEquals(Money.of(BigDecimal.valueOf(250), "USD"), order.getTotalAmount());
    }

    @Test
    void shouldUpdateOrderStatusCorrectly() {
        // Arrange
        Order order = createSampleOrder();

        // Act & Assert
        order.confirmOrder();
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());

        order.markAsPaid();
        assertEquals(OrderStatus.PAID, order.getStatus());

        order.ship();
        assertEquals(OrderStatus.SHIPPING, order.getStatus());

        order.deliver();
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDeliveredOrder() {
        // Arrange
        Order order = createSampleOrder();
        order.confirmOrder();
        order.markAsPaid();
        order.ship();
        order.deliver();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> order.cancel("test"));
    }

    @Test
    void shouldAllowItemModificationOnlyInCreatedStatus() {
        // Arrange
        Order order = createSampleOrder();
        OrderItem newItem = new OrderItem(
                "product-2",
                "Product Two",
                1,
                BigDecimal.valueOf(50));

        // Act
        order.addItem(newItem);
        assertEquals(2, order.getItems().size());

        // Confirm order and try to modify
        order.confirmOrder();
        assertThrows(IllegalStateException.class, () -> order.addItem(newItem));
    }

    @Test
    void shouldGenerateCorrectDomainEvents() {
        // Arrange
        Order order = createSampleOrder();

        // Act
        order.confirmOrder();
        order.markAsPaid();

        // Assert
        assertFalse(order.getDomainEvents().isEmpty());
    }

    private Order createSampleOrder() {
        OrderItem item = new OrderItem(
                "product-1",
                "Product One",
                2,
                BigDecimal.valueOf(100));
        Address address = new Address(
                "123 Street",
                "City",
                "State",
                "Country",
                "12345",
                "John Doe",
                "123-456-7890");
        return new Order("customer-123", Arrays.asList(item), address);
    }
}
