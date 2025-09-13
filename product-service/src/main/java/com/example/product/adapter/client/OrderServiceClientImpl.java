package com.example.product.adapter.client;

import com.example.product.application.port.out.OrderServiceClient;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class OrderServiceClientImpl implements OrderServiceClient {
    private final WebClient webClient;
    private final ReactiveCircuitBreaker circuitBreaker;

    public OrderServiceClientImpl(WebClient.Builder webClientBuilder,
                                ReactiveCircuitBreakerFactory circuitBreakerFactory) {
        this.webClient = webClientBuilder.baseUrl("http://order-service").build();
        this.circuitBreaker = circuitBreakerFactory.create("order-service");
    }

    public Mono<List<OrderDetail>> getOrdersByProductId(String productId) {
        return circuitBreaker.run(
            webClient.get()
                    .uri("/api/orders/product/{productId}", productId)
                    .retrieve()
                    .bodyToFlux(OrderDetail.class)
                    .collectList(),
            throwable -> Mono.just(Collections.emptyList())
        );
    }

    public Mono<OrderSummary> getOrderById(String orderId) {
        return circuitBreaker.run(
            webClient.get()
                    .uri("/api/orders/{orderId}", orderId)
                    .retrieve()
                    .bodyToMono(OrderSummary.class),
            throwable -> Mono.empty()
        );
    }
}
