package com.example.product.adapter.persistence;

import com.example.product.domain.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer stockQuantity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;
    
    @Column(name = "category_id")
    private String categoryId;
    
    @Column(unique = true, nullable = false)
    private String sku;
    
    @Version
    private Long version;
}
