package com.online_marketplace_api.awesomity.controllers;

import com.online_marketplace_api.awesomity.common.productDTO.ProductListResponse;
import com.online_marketplace_api.awesomity.common.productDTO.ProductRequest;
import com.online_marketplace_api.awesomity.common.productDTO.ProductResponse;
import com.online_marketplace_api.awesomity.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @Operation(summary = "Create a new product", description = "This endpoint allows users to create a new product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully created",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @Operation(summary = "Get product by ID", description = "This endpoint fetches a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Get all products", description = "This endpoint fetches a list of all products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductListResponse.class))),
    })
    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @Operation(summary = "Get products by category", description = "This endpoint allows users to fetch products by category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductListResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductListResponse> getProductsByCategory(@PathVariable Long categoryId,   @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageable));
    }

    @Operation(summary = "Get products by tags", description = "This endpoint allows users to fetch products by tags.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductListResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tags not found")
    })
    @GetMapping("/tags")
    public ResponseEntity<ProductListResponse> getProductsByTags(@RequestParam("tagIds") List<Long> tagIds, Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsByTags(tagIds, pageable));
    }

    @Operation(summary = "Search products by keyword", description = "This endpoint allows users to search products by a keyword.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductListResponse.class))),
            @ApiResponse(responseCode = "404", description = "No products found")
    })
    @GetMapping("/search")
    public ResponseEntity<ProductListResponse> searchProducts(@RequestParam("keyword") String keyword, Pageable pageable) {
        return ResponseEntity.ok(productService.searchProducts(keyword, pageable));
    }

    @Operation(summary = "Update product details", description = "This endpoint allows users to update product details by product ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully updated",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Operation(summary = "Delete a product", description = "This endpoint allows users to delete a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload product image", description = "This endpoint allows users to upload an image for a product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image successfully uploaded",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file type"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<ProductResponse> uploadProductImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(productService.uploadProductImage(id, file));
    }

    @Operation(summary = "Set product as featured", description = "This endpoint allows users to mark a product as featured.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully marked as featured",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{id}/set-featured")
    public ResponseEntity<ProductResponse> setProductAsFeatured(@PathVariable Long id, @RequestParam("featured") boolean featured) {
        return ResponseEntity.ok(productService.setProductAsFeatured(id, featured));
    }

    @Operation(summary = "Get featured products", description = "This endpoint allows users to fetch a list of featured products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of featured products",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductListResponse.class))),
    })
    @GetMapping("/featured")
    public ResponseEntity<ProductListResponse> getFeaturedProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getFeaturedProducts(pageable));
    }

    @Operation(summary = "Get products by seller", description = "This endpoint allows users to fetch products by seller ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found by seller",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductListResponse.class))),
            @ApiResponse(responseCode = "404", description = "Seller not found")
    })
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ProductListResponse> getProductsBySeller(@PathVariable Long sellerId, Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId, pageable));
    }
}
