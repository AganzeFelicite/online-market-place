package com.online_marketplace_api.awesomity.common.categoryDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data

@Getter
@Setter
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Long parentCategoryId;
    private List<CategoryResponse> subcategories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
