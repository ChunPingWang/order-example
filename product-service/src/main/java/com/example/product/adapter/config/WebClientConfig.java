package com.example.product.adapter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.order.url:http://localhost:8081}")
    private String orderServiceUrl;

    @Bean
    public WebClient orderServiceWebClient() {
        return WebClient.builder()
                .baseUrl(orderServiceUrl)
                .build();
    }
}
