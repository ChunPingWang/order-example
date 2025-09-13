package com.example.product.adapter.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.product.application.port.ProductRepository;
import com.example.product.domain.ProductStatus;
import org.springframework.data.domain.PageRequest;

@Component
@RequiredArgsConstructor
public class ProductMetricsCollector {

    private final ProductRepository productRepository;
    private final MeterRegistry registry;

    @Scheduled(fixedRate = 300000) // 每5分鐘執行一次
    public void collectProductMetrics() {
        // 收集庫存相關指標
        int lowStockCount = productRepository.findByStockQuantityLessThan(10, 0, Integer.MAX_VALUE).size();
        registry.gauge("product.low.stock.count", lowStockCount);

        // 收集不同狀態的產品數量
        for (ProductStatus status : ProductStatus.values()) {
            int count = productRepository.findByNameContainingAndStatus("", status, 0, Integer.MAX_VALUE).size();
            registry.gauge("product.status.count", 
                    "status", status.name(), 
                    count);
        }
    }
}
