package com.example.product.adapter.web;

import com.example.product.domain.Category;
import com.example.product.application.port.CategoryRepository;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @PostMapping
    @Timed(value = "categories.create", description = "Time taken to create new categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryRepository.save(category));
    }

    @GetMapping("/{id}")
    @Timed(value = "categories.get", description = "Time taken to retrieve categories")
    public ResponseEntity<Category> getCategory(@PathVariable String id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/parent/{parentId}")
    @Timed(value = "categories.getByParent", description = "Time taken to get categories by parent")
    public ResponseEntity<List<Category>> getCategoriesByParent(@PathVariable String parentId) {
        return ResponseEntity.ok(categoryRepository.findByParentId(parentId));
    }

    @PutMapping("/{id}")
    @Timed(value = "categories.update", description = "Time taken to update categories")
    public ResponseEntity<Category> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody Category category) {
        return categoryRepository.findById(id)
                .map(existing -> {
                    category.setId(existing.getId());
                    return ResponseEntity.ok(categoryRepository.save(category));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Timed(value = "categories.delete", description = "Time taken to delete categories")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
