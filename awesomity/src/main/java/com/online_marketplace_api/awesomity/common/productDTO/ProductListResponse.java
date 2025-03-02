package com.online_marketplace_api.awesomity.common.productDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> products;
    private long totalItems;
    private int totalPages;
    private int currentPage;
}
