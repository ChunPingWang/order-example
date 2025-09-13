package com.example.product.adapter.event;

import com.example.product.domain.event.ProductCreatedEvent;
import com.example.product.domain.event.ProductDiscontinuedEvent;
import com.example.product.domain.event.ProductStockUpdatedEvent;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventListener {

    private final MeterRegistry meterRegistry;

    @EventListener
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        log.info("Product created: {}", event.getProduct().getId());
        meterRegistry.counter("product.created.total").increment();
    }

    @EventListener
    public void handleProductStockUpdatedEvent(ProductStockUpdatedEvent event) {
        log.info("Product stock updated: {}, change: {}", 
            event.getProduct().getId(), event.getQuantityChange());
        
        meterRegistry.counter("product.stock.updates.total").increment();
        meterRegistry.gauge("product.stock.level", 
            event.getProduct().getStockQuantity());
    }

    @EventListener
    public void handleProductDiscontinuedEvent(ProductDiscontinuedEvent event) {
        log.info("Product discontinued: {}", event.getProduct().getId());
        meterRegistry.counter("product.discontinued.total").increment();
    }
}
