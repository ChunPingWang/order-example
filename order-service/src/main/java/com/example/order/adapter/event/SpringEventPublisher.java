package com.example.order.adapter.event;

import com.example.order.domain.event.DomainEventPublisher;
import com.example.order.domain.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringEventPublisher implements DomainEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    @Override
    public void publish(OrderEvent event) {
        eventPublisher.publishEvent(event);
    }
}
