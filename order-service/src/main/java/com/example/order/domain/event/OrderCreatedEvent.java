package com.example.order.domain.event;

public class OrderCreatedEvent extends OrderEvent {
    public OrderCreatedEvent(String orderId) {
        super(orderId);
    }
}

class OrderPaidEvent extends OrderEvent {
    public OrderPaidEvent(String orderId) {
        super(orderId);
    }
}

class OrderConfirmedEvent extends OrderEvent {
    public OrderConfirmedEvent(String orderId) {
        super(orderId);
    }
}

class OrderShippedEvent extends OrderEvent {
    private final String trackingNumber;

    public OrderShippedEvent(String orderId, String trackingNumber) {
        super(orderId);
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }
}

class OrderDeliveredEvent extends OrderEvent {
    public OrderDeliveredEvent(String orderId) {
        super(orderId);
    }
}

class OrderCancelledEvent extends OrderEvent {
    private final String reason;

    public OrderCancelledEvent(String orderId, String reason) {
        super(orderId);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
