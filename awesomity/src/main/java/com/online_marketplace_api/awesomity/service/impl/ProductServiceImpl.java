package com.online_marketplace_api.awesomity.service.impl;

import com.online_marketplace_api.awesomity.Entity.Category;
import com.online_marketplace_api.awesomity.Entity.Product;
import com.online_marketplace_api.awesomity.Entity.Tag;
import com.online_marketplace_api.awesomity.Entity.User;
import com.online_marketplace_api.awesomity.Repository.ICategoryRepository;
import com.online_marketplace_api.awesomity.Repository.IProductRepository;
import com.online_marketplace_api.awesomity.Repository.ITagRepository;
import com.online_marketplace_api.awesomity.common.exceptions.GlobalExceptionHandler;
import com.online_marketplace_api.awesomity.common.productDTO.ProductListResponse;
import com.online_marketplace_api.awesomity.common.productDTO.ProductRequest;
import com.online_marketplace_api.awesomity.common.productDTO.ProductResponse;
import com.online_marketplace_api.awesomity.enums.Role;
import com.online_marketplace_api.awesomity.security.SecurityUtils;
import com.online_marketplace_api.awesomity.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final ITagRepository tagRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            if (tags.size() != request.getTagIds().size()) {
                throw new GlobalExceptionHandler.ResourceNotFoundException("One or more tags not found");
            }
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .category(category)
                .seller(currentUser)
                .tags(tags)
                .active(true)
                .featured(false) // Only admins can set featured products
                .build();

        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = getProductEntityById(id);
        return mapToProductResponse(product);
    }

    @Override
    public ProductListResponse getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findByActiveTrue( pageable);
        return createProductListResponse(productPage, pageable);
    }

    @Override
    public ProductListResponse getProductsByCategory(Long categoryId, Pageable pageable) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Category not found with id: " + categoryId));

        Page<Product> productPage = productRepository.findByCategoryIdAndActiveTrue(categoryId,  pageable);
        return createProductListResponse(productPage, pageable);
    }

    @Override
    public ProductListResponse getProductsByTags(List<Long> tagIds, Pageable pageable) {
        Page<Product> productPage = productRepository.findByTagsIdInAndActiveTrue((Set<Long>) tagIds, pageable);
        return createProductListResponse(productPage, pageable);
    }

    @Override
    public ProductListResponse searchProducts(String keyword, Pageable pageable) {
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword, pageable);
        return createProductListResponse(productPage, pageable);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = getProductEntityById(id);
        User currentUser = SecurityUtils.getCurrentUser();

        // Check if user is product owner or admin
        if (!product.getSeller().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new GlobalExceptionHandler.UnauthorizedException("You don't have permission to update this product");
        }

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            product.setCategory(category);
        }
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            if (tags.size() != request.getTagIds().size()) {
                throw new GlobalExceptionHandler.ResourceNotFoundException("One or more tags not found");
            }
            product.setTags(tags);
        }

        return mapToProductResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductEntityById(id);
        User currentUser = SecurityUtils.getCurrentUser();

        // Check if user is product owner or admin
        if (!product.getSeller().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new GlobalExceptionHandler.UnauthorizedException("You don't have permission to delete this product");
        }

        // Soft delete - set active to false
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductResponse uploadProductImage(Long id, MultipartFile file) {
        Product product = getProductEntityById(id);
        User currentUser = SecurityUtils.getCurrentUser();

        // Check if user is product owner or admin
        if (!product.getSeller().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new GlobalExceptionHandler.UnauthorizedException("You don't have permission to update this product");
        }

        String imageUrl = fileStorageService.storeFile(file);
        product.setImageUrl(imageUrl);

        return mapToProductResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse setProductAsFeatured(Long id, boolean featured) {
        Product product = getProductEntityById(id);
        User currentUser = SecurityUtils.getCurrentUser();

        // Only admins can set products as featured
        if (currentUser.getRole() != Role.ADMIN) {
            throw new GlobalExceptionHandler.UnauthorizedException("Only admins can set products as featured");
        }

        product.setFeatured(featured);
        return mapToProductResponse(productRepository.save(product));
    }

    @Override
    public ProductListResponse getFeaturedProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findByFeaturedTrue( pageable);
        return createProductListResponse(productPage, pageable);
    }

    @Override
    public ProductListResponse getProductsBySeller(Long sellerId, Pageable pageable) {
        Page<Product> productPage = productRepository.findBySellerId(sellerId, pageable);
        return createProductListResponse(productPage, pageable);
    }

    // Helper methods
    private Product getProductEntityById(Long id) {
        return (Product) productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Product not found with id: " + id));
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .imageUrl(product.getImageUrl())
                .featured(product.isFeatured())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .sellerId(product.getSeller().getId())
                .sellerName(product.getSeller().getFirstName() + " " + product.getSeller().getLastName())
                .averageRating(product.getAverageRating())
                .tags(product.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private ProductListResponse createProductListResponse(Page<Product> productPage, Pageable pageable) {
        List<ProductResponse> products = productPage.getContent().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());

        return ProductListResponse.builder()
                .products(products)
                .totalItems((productPage.getTotalElements()))
                .totalPages(productPage.getTotalPages())
                .currentPage(pageable.getPageNumber())
                .build();
    }
}