package com.example.order.adapter.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class OrderMetrics {
    
    private final MeterRegistry registry;
    private Counter orderCreatedCounter;
    private Counter orderCancelledCounter;
    private Counter orderPaidCounter;
    private AtomicInteger activeOrders;
    private Timer orderProcessingTimer;
    
    @PostConstruct
    public void init() {
        orderCreatedCounter = Counter.builder("orders.created")
                .description("The number of orders created")
                .register(registry);
                
        orderCancelledCounter = Counter.builder("orders.cancelled")
                .description("The number of orders cancelled")
                .register(registry);
                
        orderPaidCounter = Counter.builder("orders.paid")
                .description("The number of orders paid")
                .register(registry);
                
        activeOrders = registry.gauge("orders.active",
                new AtomicInteger(0));
                
        orderProcessingTimer = Timer.builder("orders.processing")
                .description("Time taken to process orders")
                .register(registry);
    }
    
    public void incrementOrderCreated() {
        orderCreatedCounter.increment();
        activeOrders.incrementAndGet();
    }
    
    public void incrementOrderCancelled() {
        orderCancelledCounter.increment();
        activeOrders.decrementAndGet();
    }
    
    public void incrementOrderPaid() {
        orderPaidCounter.increment();
    }
    
    public Timer getOrderProcessingTimer() {
        return orderProcessingTimer;
    }
}
