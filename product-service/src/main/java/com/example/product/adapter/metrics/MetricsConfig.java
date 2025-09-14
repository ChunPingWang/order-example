package com.example.product.adapter.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public InventoryMetrics inventoryMetrics(MeterRegistry registry) {
        return new InventoryMetrics(registry);
    }
}

class InventoryMetrics {
    private final MeterRegistry registry;

    public InventoryMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordStockIncrease(String productId, int amount) {
        registry.counter("inventory.stock.increase",
                "product_id", productId
        ).increment(amount);
    }

    public void recordStockDecrease(String productId, int amount) {
        registry.counter("inventory.stock.decrease",
                "product_id", productId
        ).increment(amount);
    }

    public void recordStockReservation(String productId, int amount) {
        registry.counter("inventory.stock.reserve",
                "product_id", productId
        ).increment(amount);
    }

    public void recordReservationCancellation(String productId, int amount) {
        registry.counter("inventory.stock.reserve.cancel",
                "product_id", productId
        ).increment(amount);
    }

    public void recordReservationConfirmation(String productId, int amount) {
        registry.counter("inventory.stock.reserve.confirm",
                "product_id", productId
        ).increment(amount);
    }

    public void updateStockLevel(String productId, int currentStock) {
        registry.gauge("inventory.stock.level",
                java.util.List.of(java.util.Map.entry("product_id", productId)),
                currentStock);
    }

    public void recordOperationLatency(String operation, long latencyMs) {
        registry.timer("inventory.operation.latency",
                "operation", operation
        ).record(java.time.Duration.ofMillis(latencyMs));
    }
}
