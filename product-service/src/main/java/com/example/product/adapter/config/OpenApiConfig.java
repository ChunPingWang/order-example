package com.example.product.adapter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Product Service API")
                .description("Product service for e-commerce system")
                .version("v1.0")
                .contact(new Contact()
                    .name("Development Team")
                    .email("dev@example.com")));
    }
