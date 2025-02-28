package com.online_marketplace_api.awesomity.controllers;

import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewListResponse;
import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewRequest;
import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewResponse;
import com.online_marketplace_api.awesomity.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "APIs for managing product reviews.")
public class ReviewController {

    private final IReviewService reviewService;

    @PostMapping("/product/{productId}")
    @Operation(summary = "Create a review for a product", description = "Create a new review for the specified product.")
    public ResponseEntity<ReviewResponse> createReview(
            @Parameter(description = "ID of the product to review") @PathVariable Long productId,
            @Parameter(description = "Review details to be created") @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(productId, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get review by ID", description = "Fetch a review by its ID.")
    public ResponseEntity<ReviewResponse> getReviewById(
            @Parameter(description = "ID of the review to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get reviews for a product", description = "Fetch a list of reviews for a specific product with pagination.")
    public ResponseEntity<ReviewListResponse> getReviewsByProduct(
            @Parameter(description = "ID of the product for which reviews are fetched") @PathVariable Long productId,
            @Parameter(description = "Pagination details") Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId, pageable));
    }

    @GetMapping("/user")
    @Operation(summary = "Get reviews by the current user", description = "Fetch reviews submitted by the logged-in user.")
    public ResponseEntity<ReviewListResponse> getUserReviews(@Parameter(description = "Pagination details") Pageable pageable) {
        return ResponseEntity.ok(reviewService.getUserReviews(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a review", description = "Update an existing review by its ID.")
    public ResponseEntity<ReviewResponse> updateReview(
            @Parameter(description = "ID of the review to update") @PathVariable Long id,
            @Parameter(description = "Updated review details") @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review", description = "Delete a review by its ID.")
    public ResponseEntity<Void> deleteReview(@Parameter(description = "ID of the review to delete") @PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
