package com.example.product.domain.event;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
