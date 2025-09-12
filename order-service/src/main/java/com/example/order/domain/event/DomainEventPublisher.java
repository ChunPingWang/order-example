package com.example.order.domain.event;

public interface DomainEventPublisher {
    void publish(OrderEvent event);
}
