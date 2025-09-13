package com.example.product.adapter.web;

import com.example.product.application.command.ProductCommands.*;
import com.example.product.domain.Product;
import com.example.product.domain.ProductStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenCreateProduct_thenSuccess() throws Exception {
        CreateProductCommand command = new CreateProductCommand(
            "Test Product",
            "Test Description",
            new BigDecimal("99.99"),
            100,
            "category-1",
            "TEST-SKU-001"
        );

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(command.getName())))
                .andExpect(jsonPath("$.sku", is(command.getSku())))
                .andExpect(jsonPath("$.status", is(ProductStatus.IN_STOCK.name())));
    }

    @Test
    void whenUpdateStock_thenSuccess() throws Exception {
        // First create a product
        CreateProductCommand createCommand = new CreateProductCommand(
            "Stock Test Product",
            "Description",
            new BigDecimal("19.99"),
            50,
            "category-1",
            "TEST-SKU-002"
        );

        String createResponse = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommand)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Product createdProduct = objectMapper.readValue(createResponse, Product.class);

        // Then update its stock
        UpdateStockQuantityCommand updateCommand = new UpdateStockQuantityCommand(
            createdProduct.getId(),
            -10
        );

        mockMvc.perform(put("/api/products/{id}/stock", createdProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity", is(40)));
    }

    @Test
    void whenSearchProducts_thenSuccess() throws Exception {
        // Create a test product first
        CreateProductCommand command = new CreateProductCommand(
            "Search Test Product",
            "Description",
            new BigDecimal("29.99"),
            30,
            "category-1",
            "TEST-SKU-003"
        );

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated());

        // Then search for it
        mockMvc.perform(get("/api/products/search")
                .param("keyword", "Search")
                .param("status", ProductStatus.IN_STOCK.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", containsString("Search")));
    }

    @Test
    void whenGetLowStockProducts_thenSuccess() throws Exception {
        // Create a product with low stock
        CreateProductCommand command = new CreateProductCommand(
            "Low Stock Product",
            "Description",
            new BigDecimal("39.99"),
            5,
            "category-1",
            "TEST-SKU-004"
        );

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated());

        // Then get low stock products
        mockMvc.perform(get("/api/products/low-stock")
                .param("threshold", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].stockQuantity", lessThan(10)));
    }
}
