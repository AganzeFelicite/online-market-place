package com.online_marketplace_api.awesomity.Repository;

import com.online_marketplace_api.awesomity.Entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByFeaturedTrue(Pageable pageable);

    Page<Product> findBySellerId(Long sellerId, Pageable pageable);

    Page<Product> findByActiveTrue(Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Product> search(String query, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t.name IN :tags AND p.active = true")
    Page<Product> findByTagNames(List<String> tags, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.tags t " +
            "WHERE p.category.id = :categoryId AND t.name IN :tags AND p.active = true")
    Page<Product> findByCategoryIdAndTagNames(Long categoryId, List<String> tags, Pageable pageable);

    Page<Product> findByCategoryIdAndActiveTrue(Long categoryId, Pageable pageable);

    Page<Product> findByTagsIdInAndActiveTrue(Set<Long> tagIds, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String keyword, Pageable pageable);


    Optional<Object> findByIdAndActiveTrue(Long id);
}
