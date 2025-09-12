package com.example.order.application.usecase;

import com.example.order.application.command.OrderCommands.*;
import com.example.order.application.port.OrderRepository;
import com.example.order.domain.*;
import com.example.order.domain.service.OrderDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderCommandUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDomainService orderDomainService;

    private OrderCommandUseCase orderCommandUseCase;
    private Address address;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        orderCommandUseCase = new OrderCommandUseCase(orderRepository, orderDomainService);
        address = new Address(
                "123 Street",
                "City",
                "State",
                "Country",
                "12345",
                "John Doe",
                "123-456-7890");
        orderItem = new OrderItem(
                "product-1",
                "Product One",
                2,
                BigDecimal.valueOf(100));
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        CreateOrderCommand command = new CreateOrderCommand(
                "customer-123",
                Arrays.asList(orderItem),
                address);

        Order mockOrder = new Order(
                command.getCustomerId(),
                command.getItems(),
                command.getShippingAddress());

        when(orderDomainService.createOrder(
                command.getCustomerId(),
                command.getItems(),
                command.getShippingAddress()))
                .thenReturn(mockOrder);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        // Act
        Order result = orderCommandUseCase.createOrder(command);

        // Assert
        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldUpdateOrderSuccessfully() {
        // Arrange
        String orderId = "order-123";
        UpdateOrderCommand command = new UpdateOrderCommand(
                orderId,
                Arrays.asList(orderItem));

        Order existingOrder = new Order(
                "customer-123",
                Arrays.asList(orderItem),
                address);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(existingOrder);

        // Act
        Order result = orderCommandUseCase.updateOrder(command);

        // Assert
        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldCancelOrderSuccessfully() {
        // Arrange
        String orderId = "order-123";
        String reason = "Customer request";
        CancelOrderCommand command = new CancelOrderCommand(orderId, reason);

        Order existingOrder = new Order(
                "customer-123",
                Arrays.asList(orderItem),
                address);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(existingOrder);

        // Act
        Order result = orderCommandUseCase.cancelOrder(command);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.CANCELLED, result.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        String orderId = "non-existent-order";
        UpdateOrderCommand command = new UpdateOrderCommand(
                orderId,
                Arrays.asList(orderItem));

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> orderCommandUseCase.updateOrder(command));
    }

    @Test
    void shouldProcessOrderStatusTransitionsCorrectly() {
        // Arrange
        String orderId = "order-123";
        Order order = new Order(
                "customer-123",
                Arrays.asList(orderItem),
                address);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        // Act & Assert
        // Confirm Order
        orderCommandUseCase.confirmOrder(new ConfirmOrderCommand(orderId));
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());

        // Mark as Paid
        orderCommandUseCase.markOrderAsPaid(new MarkOrderAsPaidCommand(orderId));
        assertEquals(OrderStatus.PAID, order.getStatus());

        // Ship Order
        orderCommandUseCase.shipOrder(new ShipOrderCommand(orderId, "tracking-123"));
        assertEquals(OrderStatus.SHIPPING, order.getStatus());

        // Deliver Order
        orderCommandUseCase.deliverOrder(new DeliverOrderCommand(orderId));
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }
}
