package com.online_marketplace_api.awesomity.service.impl;

import com.online_marketplace_api.awesomity.Entity.Product;
import com.online_marketplace_api.awesomity.Entity.Review;
import com.online_marketplace_api.awesomity.Entity.User;
import com.online_marketplace_api.awesomity.Repository.IOrderRepository;
import com.online_marketplace_api.awesomity.Repository.IProductRepository;
import com.online_marketplace_api.awesomity.Repository.IReviewRepository;
import com.online_marketplace_api.awesomity.common.exceptions.GlobalExceptionHandler;
import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewListResponse;
import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewRequest;
import com.online_marketplace_api.awesomity.common.reviewDTO.ReviewResponse;
import com.online_marketplace_api.awesomity.service.IReviewService;
import com.online_marketplace_api.awesomity.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final IProductRepository productRepository;
    private final IOrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public ReviewResponse createReview(Long productId, ReviewRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Product not found with id: " + productId));

        if (!hasUserPurchasedProduct(productId)) {
            throw new GlobalExceptionHandler.UnauthorizedException("You can only review products you have purchased");
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(getAuthenticatedUser());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);
        return modelMapper.map(savedReview, ReviewResponse.class);
    }

    @Override
    public ReviewResponse getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Review not found with id: " + id));
        return modelMapper.map(review, ReviewResponse.class);
    }

    @Override
    public ReviewListResponse getReviewsByProduct(Long productId, Pageable pageable) {
        return (ReviewListResponse) reviewRepository.findByProductId(productId,  pageable);
    }

    @Override
    public ReviewListResponse getUserReviews(Pageable pageable) {
        return (ReviewListResponse) reviewRepository.findByUserId(getAuthenticatedUser().getId(),  pageable);
    }



    @Override
    @Transactional
    public ReviewResponse updateReview(Long id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Review not found with id: " + id));

        if (!review.getUser().getId().equals(getAuthenticatedUser().getId())) {
            throw new GlobalExceptionHandler.UnauthorizedException("You are not authorized to update this review");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);
        return modelMapper.map(updatedReview, ReviewResponse.class);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Review not found with id: " + id));

        if (!review.getUser().getId().equals(getAuthenticatedUser().getId())) {
            throw new GlobalExceptionHandler.UnauthorizedException("You are not authorized to delete this review");
        }

        reviewRepository.delete(review);
    }

    @Override
    public boolean hasUserPurchasedProduct(Long productId) {
        User user = getAuthenticatedUser();
        return orderRepository.existsByUserIdAndProductId(user.getId(), productId);
    }

    private User getAuthenticatedUser() {
        return SecurityUtils.getCurrentUser();
    }
}