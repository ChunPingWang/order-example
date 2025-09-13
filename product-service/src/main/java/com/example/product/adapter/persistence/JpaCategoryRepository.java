package com.example.product.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, String> {
    List<CategoryEntity> findByParentId(String parentId);
}
