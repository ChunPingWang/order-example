package com.example.product.adapter.persistence;

import com.example.product.domain.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    
    public Category toDomain(CategoryEntity entity) {
        return new Category(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getParentId()
        );
    }
    
    public CategoryEntity toEntity(Category category) {
        return new CategoryEntity(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getParentId(),
            null  // version
        );
    }
}
