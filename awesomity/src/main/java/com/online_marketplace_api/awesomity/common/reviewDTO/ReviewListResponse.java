package com.online_marketplace_api.awesomity.common.reviewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ReviewListResponse {
    private List<ReviewResponse> reviews;
}
