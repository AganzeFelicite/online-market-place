package com.online_marketplace_api.awesomity.common.categoryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CategoryRequest {
    private String name;
    private String description;
    private Long parentCategoryId; // Optional for subcategories
}
