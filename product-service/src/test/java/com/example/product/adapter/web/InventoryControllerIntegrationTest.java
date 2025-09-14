package com.example.product.adapter.web;

import com.example.product.adapter.persistence.InventoryJpaEntity;
import com.example.product.adapter.persistence.InventoryJpaRepository;
import com.example.product.application.port.in.InventoryUseCase.InventoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InventoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InventoryJpaRepository inventoryRepository;

    private static final String PRODUCT_ID = "test-product";

    @BeforeEach
    void setUp() {
        InventoryJpaEntity inventory = new InventoryJpaEntity();
        inventory.setProductId(PRODUCT_ID);
        inventory.setQuantity(100);
        inventory.setReservedQuantity(0);
        inventory.setAvailableQuantity(100);
        inventory.setAlertThreshold(20);
        inventoryRepository.save(inventory);
    }

    @Test
    void whenIncreaseStock_thenReturn200() throws Exception {
        InventoryController.IncreaseStockRequest request = 
            new InventoryController.IncreaseStockRequest(50, "Restock", "admin");

        mockMvc.perform(post("/api/v1/inventory/{productId}/increase", PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        InventoryJpaEntity updatedInventory = inventoryRepository.findByProductId(PRODUCT_ID).orElseThrow();
        assertEquals(150, updatedInventory.getQuantity());
        assertEquals(150, updatedInventory.getAvailableQuantity());
    }

    @Test
    void whenReserveStock_thenReturn200AndUpdateStock() throws Exception {
        InventoryController.ReserveStockRequest request = 
            new InventoryController.ReserveStockRequest(30, "Order pending", "admin");

        mockMvc.perform(post("/api/v1/inventory/{productId}/reserve", PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        InventoryJpaEntity updatedInventory = inventoryRepository.findByProductId(PRODUCT_ID).orElseThrow();
        assertEquals(100, updatedInventory.getQuantity());
        assertEquals(70, updatedInventory.getAvailableQuantity());
        assertEquals(30, updatedInventory.getReservedQuantity());
    }

    @Test
    void whenGetInventory_thenReturnInventoryDetails() throws Exception {
        String response = mockMvc.perform(get("/api/v1/inventory/{productId}", PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        InventoryResponse inventory = objectMapper.readValue(response, InventoryResponse.class);
        assertEquals(PRODUCT_ID, inventory.productId());
        assertEquals(100, inventory.quantity());
        assertEquals(0, inventory.reservedQuantity());
        assertEquals(100, inventory.availableQuantity());
        assertEquals(20, inventory.alertThreshold());
    }

    @Test
    void whenDecreaseStockBelowAvailable_thenReturn500() throws Exception {
        InventoryController.DecreaseStockRequest request = 
            new InventoryController.DecreaseStockRequest(150, "Over decrease", "admin");

        mockMvc.perform(post("/api/v1/inventory/{productId}/decrease", PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());

        InventoryJpaEntity unchangedInventory = inventoryRepository.findByProductId(PRODUCT_ID).orElseThrow();
        assertEquals(100, unchangedInventory.getQuantity());
        assertEquals(100, unchangedInventory.getAvailableQuantity());
    }
}
