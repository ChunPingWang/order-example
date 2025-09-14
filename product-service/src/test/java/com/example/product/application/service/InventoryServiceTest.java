package com.example.product.application.service;

import com.example.product.application.port.in.InventoryUseCase;
import com.example.product.application.port.out.InventoryRepository;
import com.example.product.domain.Inventory;
import com.example.product.domain.event.InventoryLowStockEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    
    @Mock
    private InventoryHistoryRepository inventoryHistoryRepository;
    
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private InventoryService inventoryService;
    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService(inventoryRepository, inventoryHistoryRepository, eventPublisher);
        testInventory = new Inventory("test-product", 100, 20);
    }

    @Test
    void whenIncreaseStock_thenInventoryIsUpdatedAndHistoryIsSaved() {
        // Given
        when(inventoryRepository.findByProductId("test-product")).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));
        
        // When
        inventoryService.increaseStock(new InventoryUseCase.IncreaseStockCommand(
                "test-product", 50, "Restock", "admin"
        ));

        // Then
        verify(inventoryRepository).save(any(Inventory.class));
        verify(inventoryHistoryRepository).save(any(InventoryHistory.class));
        verify(eventPublisher, never()).publishEvent(any(InventoryLowStockEvent.class));
    }

    @Test
    void whenDecreaseStockBelowThreshold_thenLowStockEventIsPublished() {
        // Given
        when(inventoryRepository.findByProductId("test-product")).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));
        
        // When
        inventoryService.decreaseStock(new InventoryUseCase.DecreaseStockCommand(
                "test-product", 90, "Order fulfilled", "admin"
        ));

        // Then
        verify(inventoryRepository).save(any(Inventory.class));
        verify(inventoryHistoryRepository).save(any(InventoryHistory.class));
        
        ArgumentCaptor<InventoryLowStockEvent> eventCaptor = ArgumentCaptor.forClass(InventoryLowStockEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        
        InventoryLowStockEvent capturedEvent = eventCaptor.getValue();
        assertEquals("test-product", capturedEvent.getProductId());
        assertEquals(10, capturedEvent.getCurrentStock());
        assertEquals(20, capturedEvent.getThreshold());
    }

    @Test
    void whenReserveStock_thenInventoryIsUpdatedAndHistoryIsSaved() {
        // Given
        when(inventoryRepository.findByProductId("test-product")).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));
        
        // When
        inventoryService.reserveStock(new InventoryUseCase.ReserveStockCommand(
                "test-product", 30, "Order pending", "admin"
        ));

        // Then
        verify(inventoryRepository).save(any(Inventory.class));
        verify(inventoryHistoryRepository).save(any(InventoryHistory.class));
        
        ArgumentCaptor<Inventory> inventoryCaptor = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryRepository).save(inventoryCaptor.capture());
        
        Inventory savedInventory = inventoryCaptor.getValue();
        assertEquals(30, savedInventory.getReservedQuantity());
        assertEquals(70, savedInventory.getAvailableQuantity());
    }

    @Test
    void whenProductNotFound_thenThrowException() {
        // Given
        when(inventoryRepository.findByProductId("non-existing")).thenReturn(Optional.empty());
        
        // Then
        assertThrows(IllegalStateException.class, () ->
            inventoryService.increaseStock(new InventoryUseCase.IncreaseStockCommand(
                    "non-existing", 50, "Restock", "admin"
            ))
        );
    }

    @Test
    void whenGetInventory_thenReturnInventoryResponse() {
        // Given
        when(inventoryRepository.findByProductId("test-product")).thenReturn(Optional.of(testInventory));
        
        // When
        InventoryUseCase.InventoryResponse response = inventoryService.getInventory("test-product");
        
        // Then
        assertEquals("test-product", response.productId());
        assertEquals(100, response.quantity());
        assertEquals(0, response.reservedQuantity());
        assertEquals(100, response.availableQuantity());
        assertEquals(20, response.alertThreshold());
        assertFalse(response.isLowStock());
    }
}
