package com.online_marketplace_api.awesomity.service.impl;

import com.online_marketplace_api.awesomity.Entity.Category;
import com.online_marketplace_api.awesomity.Repository.ICategoryRepository;
import com.online_marketplace_api.awesomity.common.categoryDTO.CategoryListResponse;
import com.online_marketplace_api.awesomity.common.categoryDTO.CategoryRequest;
import com.online_marketplace_api.awesomity.common.categoryDTO.CategoryResponse;

import com.online_marketplace_api.awesomity.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    @Autowired
    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category parentCategory = null;

        // Check if there's a parent category
        if (request.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent Category not found"));
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setParentCategory(parentCategory);  // Setting parent category if available
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        Category savedCategory = categoryRepository.save(category);

        return toCategoryResponse(savedCategory);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return toCategoryResponse(category);
    }

    @Override
    public CategoryListResponse getAllCategories(Pageable pageable) {
        Page<Category> categoriesPage = categoryRepository.findAll(pageable);
        List<CategoryResponse> categoryResponses = categoriesPage.getContent().stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());

        return new CategoryListResponse(categoryResponses);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        // Handle updating parent category if necessary
        if (request.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent Category not found"));
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);  // No parent if ID is null
        }

        category.setUpdatedAt(LocalDateTime.now());

        Category updatedCategory = categoryRepository.save(category);

        return toCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    // Helper method to convert Category entity to CategoryResponse DTO
    private CategoryResponse toCategoryResponse(Category category) {
        List<CategoryResponse> subcategories = category.getSubcategories().stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentCategoryId(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
                .subcategories(subcategories)
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
