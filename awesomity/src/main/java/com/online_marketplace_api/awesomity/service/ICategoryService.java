package com.online_marketplace_api.awesomity.service;

import com.online_marketplace_api.awesomity.common.categoryDTO.CategoryListResponse;
import com.online_marketplace_api.awesomity.common.categoryDTO.CategoryRequest;
import com.online_marketplace_api.awesomity.common.categoryDTO.CategoryResponse;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse getCategoryById(Long id);
    CategoryListResponse getAllCategories(Pageable pageable);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}