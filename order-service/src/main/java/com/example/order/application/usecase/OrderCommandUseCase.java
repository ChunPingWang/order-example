package com.example.order.application.usecase;

import com.example.order.application.command.OrderCommands.*;
import com.example.order.application.port.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.service.OrderDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderCommandUseCase {
    
    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;
    
    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        Order order = orderDomainService.createOrder(
                command.getCustomerId(),
                command.getItems(),
                command.getShippingAddress());
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order updateOrder(UpdateOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
                
        command.getItems().forEach(order::addItem);
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order cancelOrder(CancelOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
                
        order.cancel(command.getReason());
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order confirmOrder(ConfirmOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
                
        order.confirmOrder();
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order markOrderAsPaid(MarkOrderAsPaidCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
                
        order.markAsPaid();
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order shipOrder(ShipOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
                
        order.ship();
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order deliverOrder(DeliverOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
                
        order.deliver();
        return orderRepository.save(order);
    }
}
