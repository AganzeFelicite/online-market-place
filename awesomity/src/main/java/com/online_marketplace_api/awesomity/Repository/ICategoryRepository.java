package com.online_marketplace_api.awesomity.Repository;

import com.online_marketplace_api.awesomity.Entity.Category;
import com.online_marketplace_api.awesomity.Entity.Product;
import com.online_marketplace_api.awesomity.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> , JpaSpecificationExecutor<Product> {
    Optional<Category> findByName(String name);
    List<Category> findByParentCategoryIsNull();
    List<Category> findByParentCategoryId(Long parentId);
}