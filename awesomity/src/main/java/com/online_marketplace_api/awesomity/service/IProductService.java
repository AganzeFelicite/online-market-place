package com.online_marketplace_api.awesomity.service;
import com.online_marketplace_api.awesomity.common.productDTO.ProductListResponse;
import com.online_marketplace_api.awesomity.common.productDTO.ProductRequest;
import com.online_marketplace_api.awesomity.common.productDTO.ProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    ProductListResponse getAllProducts(Pageable pageable);
    ProductListResponse getProductsByCategory(Long categoryId, Pageable pageable);
    ProductListResponse getProductsByTags(List<Long> tagIds, Pageable pageable);
    ProductListResponse searchProducts(String keyword, Pageable pageable);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    ProductResponse uploadProductImage(Long id, MultipartFile file);
    ProductResponse setProductAsFeatured(Long id, boolean featured);
    ProductListResponse getFeaturedProducts(Pageable pageable);
    ProductListResponse getProductsBySeller(Long sellerId, Pageable pageable);
}