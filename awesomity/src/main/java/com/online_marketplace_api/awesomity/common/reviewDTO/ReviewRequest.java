package com.online_marketplace_api.awesomity.common.reviewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ReviewRequest {
    private Long userId;
    private Long productId;
    private String comment;
    private Integer rating;
}
