package com.example.product.application.port;

import com.example.product.domain.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(String id);
    List<Category> findByParentId(String parentId);
    void deleteById(String id);
}
