package com.online_marketplace_api.awesomity.common.productDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String imageUrl;
    private boolean featured;
    private Long categoryId;
    private String categoryName;
    private Long sellerId;
    private String sellerName;
    private Set<String> tags;
    private Double averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
}
