package com.example.order.application.usecase;

import com.example.order.application.port.OrderRepository;
import com.example.order.application.query.OrderQueries.*;
import com.example.order.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderQueryUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderQueryUseCase orderQueryUseCase;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        orderQueryUseCase = new OrderQueryUseCase(orderRepository);
        
        OrderItem orderItem = new OrderItem(
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
                
        sampleOrder = new Order(
                "customer-123",
                Arrays.asList(orderItem),
                address);
    }

    @Test
    void shouldGetOrderByIdSuccessfully() {
        // Arrange
        String orderId = "order-123";
        GetOrderQuery query = new GetOrderQuery(orderId);
        
        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(sampleOrder));

        // Act
        Order result = orderQueryUseCase.getOrder(query);

        // Assert
        assertNotNull(result);
        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        String orderId = "non-existent-order";
        GetOrderQuery query = new GetOrderQuery(orderId);
        
        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> orderQueryUseCase.getOrder(query));
    }

    @Test
    void shouldGetCustomerOrdersSuccessfully() {
        // Arrange
        String customerId = "customer-123";
        GetCustomerOrdersQuery query = new GetCustomerOrdersQuery(
                customerId,
                null,
                0,
                10);
                
        List<Order> expectedOrders = Arrays.asList(sampleOrder);
        
        when(orderRepository.findByCustomerId(customerId))
                .thenReturn(expectedOrders);

        // Act
        List<Order> results = orderQueryUseCase.getCustomerOrders(query);

        // Assert
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(orderRepository).findByCustomerId(customerId);
    }

    @Test
    void shouldSearchOrdersSuccessfully() {
        // Arrange
        String keyword = "product";
        SearchOrdersQuery query = new SearchOrdersQuery(
                keyword,
                null,
                null,
                null,
                0,
                10);
                
        List<Order> expectedOrders = Arrays.asList(sampleOrder);
        
        when(orderRepository.findByCustomerId(keyword))
                .thenReturn(expectedOrders);

        // Act
        List<Order> results = orderQueryUseCase.searchOrders(query);

        // Assert
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(orderRepository).findByCustomerId(keyword);
    }
}
