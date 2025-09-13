package com.example.product.adapter.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ProductMetricsAspect {

    private final MeterRegistry registry;

    @Around("execution(* com.example.product.application.service.ProductCommandService.*(..))")
    public Object measureCommandExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return Timer.builder("product.command.execution")
                .tag("method", joinPoint.getSignature().getName())
                .register(registry)
                .record(() -> {
                    try {
                        return joinPoint.proceed();
                    } catch (Throwable t) {
                        registry.counter("product.command.errors",
                                "method", joinPoint.getSignature().getName(),
                                "type", t.getClass().getSimpleName())
                                .increment();
                        throw new RuntimeException(t);
                    }
                });
    }

    @Around("execution(* com.example.product.application.service.ProductQueryService.*(..))")
    public Object measureQueryExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return Timer.builder("product.query.execution")
                .tag("method", joinPoint.getSignature().getName())
                .register(registry)
                .record(() -> {
                    try {
                        return joinPoint.proceed();
                    } catch (Throwable t) {
                        registry.counter("product.query.errors",
                                "method", joinPoint.getSignature().getName(),
                                "type", t.getClass().getSimpleName())
                                .increment();
                        throw new RuntimeException(t);
                    }
                });
    }
}
