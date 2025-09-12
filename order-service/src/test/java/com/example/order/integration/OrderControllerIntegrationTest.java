package com.example.order.integration;

import com.example.order.adapter.web.OrderController;
import com.example.order.application.command.OrderCommands.*;
import com.example.order.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class OrderControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderController orderController;

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {
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

        CreateOrderCommand command = new CreateOrderCommand(
                "customer-123",
                Arrays.asList(item),
                address);

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customerId").value("customer-123"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void shouldRetrieveOrderSuccessfully() throws Exception {
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

        CreateOrderCommand command = new CreateOrderCommand(
                "customer-123",
                Arrays.asList(item),
                address);

        var response = orderController.createOrder(command);
        String orderId = response.getBody().getId();

        // Act & Assert
        mockMvc.perform(get("/api/orders/{orderId}", orderId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void shouldUpdateOrderStatusSuccessfully() throws Exception {
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

        CreateOrderCommand command = new CreateOrderCommand(
                "customer-123",
                Arrays.asList(item),
                address);

        var response = orderController.createOrder(command);
        String orderId = response.getBody().getId();

        // Act & Assert - Confirm Order
        mockMvc.perform(post("/api/orders/{orderId}/confirm", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        // Act & Assert - Mark as Paid
        mockMvc.perform(post("/api/orders/{orderId}/pay", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void shouldRetrieveCustomerOrdersSuccessfully() throws Exception {
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
        CreateOrderCommand command1 = new CreateOrderCommand(
                customerId,
                Arrays.asList(item),
                address);
        CreateOrderCommand command2 = new CreateOrderCommand(
                customerId,
                Arrays.asList(item),
                address);

        orderController.createOrder(command1);
        orderController.createOrder(command2);

        // Act & Assert
        mockMvc.perform(get("/api/orders/customer/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldHandleOrderNotFoundScenario() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/orders/{orderId}", "non-existent-id")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldValidateCreateOrderRequest() throws Exception {
        // Arrange
        CreateOrderCommand invalidCommand = new CreateOrderCommand(
                null,
                null,
                null);

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest());
    }
}
