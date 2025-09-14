package com.example.product.adapter.event;

import com.example.product.domain.event.InventoryLowStockEvent;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryLowStockEventListener {
    
    private final MeterRegistry meterRegistry;
    
    @EventListener
    public void handleLowStockEvent(InventoryLowStockEvent event) {
        log.warn("Low stock alert for product: {}. Current stock: {}, Threshold: {}",
                event.getProductId(), event.getCurrentStock(), event.getThreshold());
        
        // 增加低庫存計數
        meterRegistry.counter("inventory.low_stock.alert",
                "product_id", event.getProductId(),
                "threshold", String.valueOf(event.getThreshold())
        ).increment();
        
        // TODO: 可以在這裡添加其他通知機制，例如：
        // 1. 發送郵件通知
        // 2. 發送 Slack 通知
        // 3. 觸發自動補貨流程
    }
}
