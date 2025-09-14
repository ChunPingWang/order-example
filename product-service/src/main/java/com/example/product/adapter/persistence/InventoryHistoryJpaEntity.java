package com.example.product.adapter.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_histories")
@Getter
@Setter
public class InventoryHistoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String productId;
    
    @Enumerated(EnumType.STRING)
    private InventoryOperation operation;
    
    private Integer quantity;
    private Integer stockBefore;
    private Integer stockAfter;
    private String reason;
    private LocalDateTime timestamp;
    private String operatedBy;
}
