package com.online_marketplace_api.awesomity.common.categoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor

public class CategoryListResponse {
    private List<CategoryResponse> categories;
}
