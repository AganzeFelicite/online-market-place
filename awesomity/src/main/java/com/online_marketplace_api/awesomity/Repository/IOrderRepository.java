package com.online_marketplace_api.awesomity.Repository;
import com.online_marketplace_api.awesomity.Entity.Order;
import com.online_marketplace_api.awesomity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserId(Long userId, Pageable pageable);

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByStatus(OrderStatus status , Pageable pageable);
    @Query("SELECT COUNT(o) > 0 FROM Order o JOIN o.items i WHERE o.user.id = :userId AND i.product.id = :productId")
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

}