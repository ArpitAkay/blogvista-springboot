package com.blogvista.backend.service;

import com.blogvista.backend.model.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
}
