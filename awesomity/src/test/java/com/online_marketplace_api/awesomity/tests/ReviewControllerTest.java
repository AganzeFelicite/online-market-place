package com.online_marketplace_api.awesomity.tests;
import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewListResponse;
import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewRequest;
import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewResponse;
import com.online_marketplace_api.awesomity.controllers.ReviewController;
import com.online_marketplace_api.awesomity.service.IReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReviewControllerTest {

    @Mock
    private IReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReview() {
        Long productId = 1L;
        ReviewRequest request = new ReviewRequest(1L, productId, "Test Comment", 5);
        ReviewResponse response = new ReviewResponse(1L, 1L, productId, "Test Comment", 5, null, null);

        when(reviewService.createReview(productId, request)).thenReturn(response);

        ResponseEntity<ReviewResponse> result = reviewController.createReview(productId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetReviewById() {
        Long id = 1L;
        ReviewResponse response = new ReviewResponse(id, 1L, 1L, "Test Comment", 5, null, null);

        when(reviewService.getReviewById(id)).thenReturn(response);

        ResponseEntity<ReviewResponse> result = reviewController.getReviewById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetReviewsByProduct() {
        Long productId = 1L;
        ReviewListResponse response = new ReviewListResponse(null);
        Pageable pageable = Pageable.ofSize(10);

        when(reviewService.getReviewsByProduct(productId, pageable)).thenReturn(response);

        ResponseEntity<ReviewListResponse> result = reviewController.getReviewsByProduct(productId, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetUserReviews() {
        ReviewListResponse response = new ReviewListResponse(null);
        Pageable pageable = Pageable.ofSize(10);

        when(reviewService.getUserReviews(pageable)).thenReturn(response);

        ResponseEntity<ReviewListResponse> result = reviewController.getUserReviews(pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testUpdateReview() {
        Long id = 1L;
        ReviewRequest request = new ReviewRequest(1L, 1L, "Updated Comment", 4);
        ReviewResponse response = new ReviewResponse(id, 1L, 1L, "Updated Comment", 4, null, null);

        when(reviewService.updateReview(id, request)).thenReturn(response);

        ResponseEntity<ReviewResponse> result = reviewController.updateReview(id, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testDeleteReview() {
        Long id = 1L;

        ResponseEntity<Void> result = reviewController.deleteReview(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(reviewService, times(1)).deleteReview(id);
    }
}
