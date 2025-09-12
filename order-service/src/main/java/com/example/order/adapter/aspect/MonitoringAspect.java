package com.example.order.adapter.aspect;

import io.micrometer.core.annotation.Timed;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MonitoringAspect {

    @Pointcut("@annotation(io.micrometer.core.annotation.Timed)")
    public void timedMethods() {}

    @Pointcut("execution(* com.example.order.adapter.web.*Controller.*(..))")
    public void controllerMethods() {}
    
    @Pointcut("execution(* com.example.order.adapter.persistence.*Repository.*(..))")
    public void repositoryMethods() {}
}
