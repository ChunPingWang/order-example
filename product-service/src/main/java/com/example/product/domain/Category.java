package com.example.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Category {
    private String id;
    private String name;
    private String description;
    private String parentId;
}
