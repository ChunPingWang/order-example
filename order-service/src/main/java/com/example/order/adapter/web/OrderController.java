package com.example.order.adapter.web;

import com.example.order.adapter.metrics.OrderMetrics;
import com.example.order.application.command.OrderCommands.*;
import com.example.order.application.query.OrderQueries.*;
import com.example.order.application.usecase.OrderCommandUseCase;
import com.example.order.application.usecase.OrderQueryUseCase;
import com.example.order.domain.Order;
import com.example.order.domain.OrderStatus;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.annotation.Timed;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderCommandUseCase commandUseCase;
    private final OrderQueryUseCase queryUseCase;
    private final OrderMetrics orderMetrics;
    
    @PostMapping
    @Timed(value = "orders.create", description = "Time taken to create new orders")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderCommand command) {
        Order order = commandUseCase.createOrder(command);
        orderMetrics.incrementOrderCreated();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(order);
    }
    
    @PutMapping("/{orderId}")
    @Timed(value = "orders.update", description = "Time taken to update orders")
    public ResponseEntity<Order> updateOrder(
            @PathVariable String orderId,
            @RequestBody UpdateOrderCommand command) {
        return ResponseEntity.ok(commandUseCase.updateOrder(command));
    }
    
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable String orderId,
            @RequestBody CancelOrderCommand command) {
        Order order = commandUseCase.cancelOrder(command);
        orderMetrics.incrementOrderCancelled();
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(
                commandUseCase.confirmOrder(new ConfirmOrderCommand(orderId)));
    }
    
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<Order> markOrderAsPaid(@PathVariable String orderId) {
        Order order = commandUseCase.markOrderAsPaid(new MarkOrderAsPaidCommand(orderId));
        orderMetrics.incrementOrderPaid();
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{orderId}/ship")
    public ResponseEntity<Order> shipOrder(
            @PathVariable String orderId,
            @RequestBody ShipOrderCommand command) {
        return ResponseEntity.ok(commandUseCase.shipOrder(command));
    }
    
    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliverOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(
                commandUseCase.deliverOrder(new DeliverOrderCommand(orderId)));
    }
    
    @GetMapping("/{orderId}")
    @Timed(value = "orders.get", description = "Time taken to retrieve an order")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(
                queryUseCase.getOrder(new GetOrderQuery(orderId)));
    }
    
    @GetMapping("/customer/{customerId}")
    @Timed(value = "orders.getCustomerOrders", description = "Time taken to retrieve customer orders")
    public ResponseEntity<List<Order>> getCustomerOrders(
            @PathVariable String customerId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                queryUseCase.getCustomerOrders(
                        new GetCustomerOrdersQuery(customerId, status, page, size)));
    }
    
    @GetMapping("/search")
    @Timed(value = "orders.search", description = "Time taken to search orders")
    public ResponseEntity<List<Order>> searchOrders(
            @RequestParam String keyword,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                queryUseCase.searchOrders(
                        new SearchOrdersQuery(keyword, status, startDate, endDate, page, size)));
    }
}
