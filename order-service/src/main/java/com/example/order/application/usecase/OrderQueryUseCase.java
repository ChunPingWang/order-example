package com.example.order.application.usecase;

import com.example.order.application.port.OrderRepository;
import com.example.order.application.query.OrderQueries.*;
import com.example.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryUseCase {
    
    private final OrderRepository orderRepository;
    
    public Order getOrder(GetOrderQuery query) {
        return orderRepository.findById(query.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
    
    public List<Order> getCustomerOrders(GetCustomerOrdersQuery query) {
        return orderRepository.findByCustomerId(query.getCustomerId());
    }
    
    public List<Order> searchOrders(SearchOrdersQuery query) {
        // 實際實現可能需要更複雜的搜索邏輯
        return orderRepository.findByCustomerId(query.getKeyword());
    }
}
