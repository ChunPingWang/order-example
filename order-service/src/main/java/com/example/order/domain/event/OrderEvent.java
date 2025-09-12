package com.example.order.domain.event;

import java.time.Instant;

public abstract class OrderEvent {
    private final String orderId;
    private final Instant occurredOn;

    protected OrderEvent(String orderId) {
        this.orderId = orderId;
        this.occurredOn = Instant.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }
}
