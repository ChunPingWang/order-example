package com.example.product.domain.event;

import java.time.Instant;
import lombok.Getter;

@Getter
public abstract class DomainEvent {
    private final String eventId;
    private final Instant occurredOn;
    
    protected DomainEvent() {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
    }
}
