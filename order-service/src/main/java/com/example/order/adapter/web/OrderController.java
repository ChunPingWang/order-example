package com.example.order.adapter.web;

import com.example.order.application.command.OrderCommands.*;
import com.example.order.application.query.OrderQueries.*;
import com.example.order.application.usecase.OrderCommandUseCase;
import com.example.order.application.usecase.OrderQueryUseCase;
import com.example.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderCommandUseCase commandUseCase;
    private final OrderQueryUseCase queryUseCase;
    
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandUseCase.createOrder(command));
    }
    
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable String orderId,
            @RequestBody UpdateOrderCommand command) {
        return ResponseEntity.ok(commandUseCase.updateOrder(command));
    }
    
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable String orderId,
            @RequestBody CancelOrderCommand command) {
        return ResponseEntity.ok(commandUseCase.cancelOrder(command));
    }
    
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(
                commandUseCase.confirmOrder(new ConfirmOrderCommand(orderId)));
    }
    
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<Order> markOrderAsPaid(@PathVariable String orderId) {
        return ResponseEntity.ok(
                commandUseCase.markOrderAsPaid(new MarkOrderAsPaidCommand(orderId)));
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
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(
                queryUseCase.getOrder(new GetOrderQuery(orderId)));
    }
    
    @GetMapping("/customer/{customerId}")
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
