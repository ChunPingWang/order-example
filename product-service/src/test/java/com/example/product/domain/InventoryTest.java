package com.example.product.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    
    private Inventory inventory;
    
    @BeforeEach
    void setUp() {
        inventory = new Inventory("test-product", 100, 20);
    }
    
    @Test
    void whenIncreaseStock_thenQuantityAndAvailableQuantityIncrease() {
        inventory.increaseStock(50);
        
        assertEquals(150, inventory.getQuantity());
        assertEquals(150, inventory.getAvailableQuantity());
        assertEquals(0, inventory.getReservedQuantity());
    }
    
    @Test
    void whenDecreaseStock_thenQuantityAndAvailableQuantityDecrease() {
        inventory.decreaseStock(30);
        
        assertEquals(70, inventory.getQuantity());
        assertEquals(70, inventory.getAvailableQuantity());
        assertEquals(0, inventory.getReservedQuantity());
    }
    
    @Test
    void whenDecreaseStockBelowAvailable_thenThrowException() {
        assertThrows(IllegalStateException.class, () -> 
            inventory.decreaseStock(150)
        );
    }
    
    @Test
    void whenReserveStock_thenReservedQuantityIncreasesAndAvailableQuantityDecreases() {
        inventory.reserveStock(30);
        
        assertEquals(100, inventory.getQuantity());
        assertEquals(70, inventory.getAvailableQuantity());
        assertEquals(30, inventory.getReservedQuantity());
    }
    
    @Test
    void whenReserveStockBelowAvailable_thenThrowException() {
        assertThrows(IllegalStateException.class, () -> 
            inventory.reserveStock(150)
        );
    }
    
    @Test
    void whenCancelReservation_thenReservedQuantityDecreasesAndAvailableQuantityIncreases() {
        inventory.reserveStock(30);
        inventory.cancelReservation(20);
        
        assertEquals(100, inventory.getQuantity());
        assertEquals(90, inventory.getAvailableQuantity());
        assertEquals(10, inventory.getReservedQuantity());
    }
    
    @Test
    void whenCancelReservationMoreThanReserved_thenThrowException() {
        inventory.reserveStock(30);
        
        assertThrows(IllegalStateException.class, () -> 
            inventory.cancelReservation(40)
        );
    }
    
    @Test
    void whenConfirmReservation_thenQuantityAndReservedQuantityDecrease() {
        inventory.reserveStock(30);
        inventory.confirmReservation(20);
        
        assertEquals(80, inventory.getQuantity());
        assertEquals(70, inventory.getAvailableQuantity());
        assertEquals(10, inventory.getReservedQuantity());
    }
    
    @Test
    void whenConfirmReservationMoreThanReserved_thenThrowException() {
        inventory.reserveStock(30);
        
        assertThrows(IllegalStateException.class, () -> 
            inventory.confirmReservation(40)
        );
    }
    
    @Test
    void whenStockBelowThreshold_thenIsLowStockReturnsTrue() {
        inventory.decreaseStock(85);
        
        assertTrue(inventory.isLowStock());
    }
    
    @Test
    void whenUpdateAlertThreshold_thenThresholdIsUpdated() {
        inventory.updateAlertThreshold(30);
        
        assertEquals(30, inventory.getAlertThreshold());
    }
    
    @Test
    void whenUpdateAlertThresholdWithNegativeValue_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> 
            inventory.updateAlertThreshold(-10)
        );
    }
}
