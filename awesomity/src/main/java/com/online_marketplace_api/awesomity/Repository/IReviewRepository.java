package com.online_marketplace_api.awesomity.Repository;
import com.online_marketplace_api.awesomity.Entity.Review;
import com.online_marketplace_api.awesomity.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    Page<Review> findByProductId(Long productId, Pageable pageable);

    Page<Review> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.user.id = :userId AND r.product.id = :productId")
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.product.id = :productId")
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRatingForProduct(Long productId);
}