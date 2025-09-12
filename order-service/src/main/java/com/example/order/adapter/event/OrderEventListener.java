package com.example.order.adapter.event;

import com.example.order.domain.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventListener {

    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Order created: {}", event.getOrderId());
        // 在這裡實現訂單創建後的業務邏輯
    }

    @EventListener
    public void handleOrderPaidEvent(OrderPaidEvent event) {
        log.info("Order paid: {}", event.getOrderId());
        // 在這裡實現訂單支付後的業務邏輯
    }

    @EventListener
    public void handleOrderConfirmedEvent(OrderConfirmedEvent event) {
        log.info("Order confirmed: {}", event.getOrderId());
        // 在這裡實現訂單確認後的業務邏輯
    }

    @EventListener
    public void handleOrderShippedEvent(OrderShippedEvent event) {
        log.info("Order shipped: {}, tracking number: {}", 
            event.getOrderId(), event.getTrackingNumber());
        // 在這裡實現訂單發貨後的業務邏輯
    }

    @EventListener
    public void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("Order delivered: {}", event.getOrderId());
        // 在這裡實現訂單送達後的業務邏輯
    }

    @EventListener
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Order cancelled: {}, reason: {}", 
            event.getOrderId(), event.getReason());
        // 在這裡實現訂單取消後的業務邏輯
    }
}
