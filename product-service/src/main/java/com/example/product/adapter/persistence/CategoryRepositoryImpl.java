package com.example.product.adapter.persistence;

import com.example.product.application.port.CategoryRepository;
import com.example.product.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    
    private final JpaCategoryRepository jpaRepository;
    private final CategoryMapper mapper;
    
    @Override
    public Category save(Category category) {
        CategoryEntity entity = mapper.toEntity(category);
        return mapper.toDomain(jpaRepository.save(entity));
    }
    
    @Override
    public Optional<Category> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Category> findByParentId(String parentId) {
        return jpaRepository.findByParentId(parentId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
