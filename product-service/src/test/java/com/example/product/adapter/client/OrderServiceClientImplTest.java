package com.example.product.adapter.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderServiceClientImplTest {

    private OrderServiceClientImpl orderServiceClient;
    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.ResponseSpec responseSpec;
    private ReactiveCircuitBreakerFactory circuitBreakerFactory;
    private ReactiveCircuitBreaker circuitBreaker;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        circuitBreakerFactory = mock(ReactiveCircuitBreakerFactory.class);
        circuitBreaker = mock(ReactiveCircuitBreaker.class);

        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(), any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(circuitBreakerFactory.create(any())).thenReturn(circuitBreaker);
        when(circuitBreaker.run(any(), any())).thenAnswer(invocation -> invocation.getArgument(0));

        orderServiceClient = new OrderServiceClientImpl(webClientBuilder, circuitBreakerFactory);
    }

    @Test
    void getOrdersByProductId_Success() {
        OrderDetail orderDetail = new OrderDetail();
        when(responseSpec.bodyToFlux(OrderDetail.class))
                .thenReturn(reactor.core.publisher.Flux.just(orderDetail));

        StepVerifier.create(orderServiceClient.getOrdersByProductId("product-1"))
                .expectNext(List.of(orderDetail))
                .verifyComplete();
    }

    @Test
    void getOrdersByProductId_WhenServiceFails_ReturnEmptyList() {
        when(responseSpec.bodyToFlux(OrderDetail.class))
                .thenReturn(reactor.core.publisher.Flux.error(new RuntimeException("Service Unavailable")));
        when(circuitBreaker.run(any(), any())).thenAnswer(invocation -> invocation.getArgument(1));

        StepVerifier.create(orderServiceClient.getOrdersByProductId("product-1"))
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }

    @Test
    void getOrderById_Success() {
        OrderSummary orderSummary = new OrderSummary();
        when(responseSpec.bodyToMono(OrderSummary.class))
                .thenReturn(Mono.just(orderSummary));

        StepVerifier.create(orderServiceClient.getOrderById("order-1"))
                .expectNext(orderSummary)
                .verifyComplete();
    }

    @Test
    void getOrderById_WhenServiceFails_ReturnEmptyMono() {
        when(responseSpec.bodyToMono(OrderSummary.class))
                .thenReturn(Mono.error(new RuntimeException("Service Unavailable")));
        when(circuitBreaker.run(any(), any())).thenAnswer(invocation -> invocation.getArgument(1));

        StepVerifier.create(orderServiceClient.getOrderById("order-1"))
                .verifyComplete();
    }
}
