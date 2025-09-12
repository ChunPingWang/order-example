package com.example.order.domain.service;

import com.example.order.domain.Address;
import com.example.order.domain.Order;
import com.example.order.domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDomainServiceTest {

    private OrderDomainService orderDomainService;
    private String customerId;
    private Address address;

    @BeforeEach
    void setUp() {
        orderDomainService = new OrderDomainService();
        customerId = "customer-123";
        address = new Address(
                "123 Street",
                "City",
                "State",
                "Country",
                "12345",
                "John Doe",
                "123-456-7890");
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        List<OrderItem> items = Arrays.asList(
                new OrderItem("product-1", "Product One", 2, BigDecimal.valueOf(100)));

        // Act
        Order order = orderDomainService.createOrder(customerId, items, address);

        // Assert
        assertNotNull(order);
        assertEquals(customerId, order.getCustomerId());
        assertEquals(1, order.getItems().size());
    }

    @Test
    void shouldThrowExceptionWhenCreatingOrderWithEmptyItems() {
        // Arrange
        List<OrderItem> items = new ArrayList<>();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> orderDomainService.createOrder(customerId, items, address));
    }

    @Test
    void shouldThrowExceptionWhenCreatingOrderWithInvalidQuantity() {
        // Arrange
        List<OrderItem> items = Arrays.asList(
                new OrderItem("product-1", "Product One", 0, BigDecimal.valueOf(100)));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> orderDomainService.createOrder(customerId, items, address));
    }

    @Test
    void shouldThrowExceptionWhenCreatingOrderWithInvalidPrice() {
        // Arrange
        List<OrderItem> items = Arrays.asList(
                new OrderItem("product-1", "Product One", 1, BigDecimal.valueOf(-100)));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> orderDomainService.createOrder(customerId, items, address));
    }

    @Test
    void shouldValidateMultipleItemsCorrectly() {
        // Arrange
        List<OrderItem> items = Arrays.asList(
                new OrderItem("product-1", "Product One", 2, BigDecimal.valueOf(100)),
                new OrderItem("product-2", "Product Two", 1, BigDecimal.valueOf(50)));

        // Act
        Order order = orderDomainService.createOrder(customerId, items, address);

        // Assert
        assertNotNull(order);
        assertEquals(2, order.getItems().size());
    }
}
