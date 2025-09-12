package com.example.order.integration;

import com.example.order.adapter.persistence.OrderJpaRepository;
import com.example.order.adapter.persistence.OrderRepositoryImpl;
import com.example.order.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderJpaRepository jpaRepository;

    @Autowired
    private OrderRepositoryImpl orderRepository;

    @Test
    void shouldSaveAndRetrieveOrder() {
        // Arrange
        OrderItem item = new OrderItem(
                "product-1",
                "Test Product",
                2,
                BigDecimal.valueOf(100));

        Address address = new Address(
                "123 Test St",
                "Test City",
                "Test State",
                "Test Country",
                "12345",
                "Test User",
                "123-456-7890");

        Order order = new Order("customer-123", Arrays.asList(item), address);

        // Act
        Order savedOrder = orderRepository.save(order);
        Optional<Order> retrievedOrder = orderRepository.findById(savedOrder.getId());

        // Assert
        assertTrue(retrievedOrder.isPresent());
        assertEquals(savedOrder.getId(), retrievedOrder.get().getId());
        assertEquals(1, retrievedOrder.get().getItems().size());
        assertEquals(item.getProductId(), retrievedOrder.get().getItems().get(0).getProductId());
    }

    @Test
    void shouldFindOrdersByCustomerId() {
        // Arrange
        OrderItem item = new OrderItem(
                "product-1",
                "Test Product",
                2,
                BigDecimal.valueOf(100));

        Address address = new Address(
                "123 Test St",
                "Test City",
                "Test State",
                "Test Country",
                "12345",
                "Test User",
                "123-456-7890");

        String customerId = "test-customer";
        Order order1 = new Order(customerId, Arrays.asList(item), address);
        Order order2 = new Order(customerId, Arrays.asList(item), address);

        orderRepository.save(order1);
        orderRepository.save(order2);

        // Act
        var customerOrders = orderRepository.findByCustomerId(customerId);

        // Assert
        assertEquals(2, customerOrders.size());
        assertTrue(customerOrders.stream()
                .allMatch(order -> order.getCustomerId().equals(customerId)));
    }

    @Test
    void shouldUpdateExistingOrder() {
        // Arrange
        OrderItem item = new OrderItem(
                "product-1",
                "Test Product",
                2,
                BigDecimal.valueOf(100));

        Address address = new Address(
                "123 Test St",
                "Test City",
                "Test State",
                "Test Country",
                "12345",
                "Test User",
                "123-456-7890");

        Order order = new Order("customer-123", Arrays.asList(item), address);
        Order savedOrder = orderRepository.save(order);

        // Act
        savedOrder.confirmOrder();
        Order updatedOrder = orderRepository.save(savedOrder);
        Optional<Order> retrievedOrder = orderRepository.findById(updatedOrder.getId());

        // Assert
        assertTrue(retrievedOrder.isPresent());
        assertEquals(OrderStatus.CONFIRMED, retrievedOrder.get().getStatus());
    }

    @Test
    void shouldDeleteOrder() {
        // Arrange
        OrderItem item = new OrderItem(
                "product-1",
                "Test Product",
                2,
                BigDecimal.valueOf(100));

        Address address = new Address(
                "123 Test St",
                "Test City",
                "Test State",
                "Test Country",
                "12345",
                "Test User",
                "123-456-7890");

        Order order = new Order("customer-123", Arrays.asList(item), address);
        Order savedOrder = orderRepository.save(order);

        // Act
        orderRepository.delete(savedOrder.getId());
        Optional<Order> deletedOrder = orderRepository.findById(savedOrder.getId());

        // Assert
        assertTrue(deletedOrder.isEmpty());
    }
}
