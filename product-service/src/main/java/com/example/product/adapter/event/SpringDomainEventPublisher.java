package com.example.product.adapter.event;

import com.example.product.domain.event.DomainEvent;
import com.example.product.domain.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    @Override
    public void publish(DomainEvent event) {
        eventPublisher.publishEvent(event);
    }
}
