package com.online_marketplace_api.awesomity.service;

import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewListResponse;
import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewRequest;
import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewResponse;
import org.springframework.data.domain.Pageable;


public interface IReviewService {
    ReviewResponse createReview(Long productId, ReviewRequest request);
    ReviewResponse getReviewById(Long id);
    ReviewListResponse getReviewsByProduct(Long productId, Pageable pageable);
    ReviewListResponse getUserReviews(Pageable pageable);
    ReviewResponse updateReview(Long id, ReviewRequest request);
    void deleteReview(Long id);
    boolean hasUserPurchasedProduct(Long productId);
}
