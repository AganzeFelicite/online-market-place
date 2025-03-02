package com.online_marketplace_api.awesomity.tests;

import com.online_marketplace_api.awesomity.common.productDTO.ProductListResponse;
import com.online_marketplace_api.awesomity.common.productDTO.ProductRequest;
import com.online_marketplace_api.awesomity.common.productDTO.ProductResponse;
import com.online_marketplace_api.awesomity.controllers.ProductController;
import com.online_marketplace_api.awesomity.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        ProductRequest request = ProductRequest.builder().name("Test Product").build();
        ProductResponse response = ProductResponse.builder().id(1L).name("Test Product").build();

        when(productService.createProduct(request)).thenReturn(response);

        ResponseEntity<ProductResponse> result = productController.createProduct(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetProductById() {
        Long id = 1L;
        ProductResponse response = ProductResponse.builder().id(id).name("Test Product").build();

        when(productService.getProductById(id)).thenReturn(response);

        ResponseEntity<ProductResponse> result = productController.getProductById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetAllProducts() {
        ProductListResponse response = ProductListResponse.builder().build();
        Pageable pageable = Pageable.ofSize(10);

        when(productService.getAllProducts(pageable)).thenReturn(response);

        ResponseEntity<ProductListResponse> result = productController.getAllProducts(pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetProductsByCategory() {
        Long categoryId = 1L;
        ProductListResponse response = ProductListResponse.builder().build();
        Pageable pageable = Pageable.ofSize(10);

        when(productService.getProductsByCategory(categoryId, pageable)).thenReturn(response);

        ResponseEntity<ProductListResponse> result = productController.getProductsByCategory(categoryId, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetProductsByTags() {
        List<Long> tagIds = Arrays.asList(1L, 2L);
        ProductListResponse response = ProductListResponse.builder().build();
        Pageable pageable = Pageable.ofSize(10);

        when(productService.getProductsByTags(tagIds, pageable)).thenReturn(response);

        ResponseEntity<ProductListResponse> result = productController.getProductsByTags(tagIds, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testSearchProducts() {
        String keyword = "test";
        ProductListResponse response = ProductListResponse.builder().build();
        Pageable pageable = Pageable.ofSize(10);

        when(productService.searchProducts(keyword, pageable)).thenReturn(response);

        ResponseEntity<ProductListResponse> result = productController.searchProducts(keyword, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testUpdateProduct() {
        Long id = 1L;
        ProductRequest request = ProductRequest.builder().name("Updated Product").build();
        ProductResponse response = ProductResponse.builder().id(id).name("Updated Product").build();

        when(productService.updateProduct(id, request)).thenReturn(response);

        ResponseEntity<ProductResponse> result = productController.updateProduct(id, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testDeleteProduct() {
        Long id = 1L;

        ResponseEntity<Void> result = productController.deleteProduct(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(productService, times(1)).deleteProduct(id);
    }

    @Test
    public void testUploadProductImage() {
        Long id = 1L;
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image".getBytes());
        ProductResponse response = ProductResponse.builder().id(id).imageUrl("test.jpg").build();

        when(productService.uploadProductImage(id, file)).thenReturn(response);

        ResponseEntity<ProductResponse> result = productController.uploadProductImage(id, file);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testSetProductAsFeatured() {
        Long id = 1L;
        boolean featured = true;
        ProductResponse response = ProductResponse.builder().id(id).featured(featured).build();

        when(productService.setProductAsFeatured(id, featured)).thenReturn(response);

        ResponseEntity<ProductResponse> result = productController.setProductAsFeatured(id, featured);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetFeaturedProducts() {
        ProductListResponse response = ProductListResponse.builder().build();
        Pageable pageable = Pageable.ofSize(10);

        when(productService.getFeaturedProducts(pageable)).thenReturn(response);

        ResponseEntity<ProductListResponse> result = productController.getFeaturedProducts(pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetProductsBySeller() {
        Long sellerId = 1L;
        ProductListResponse response = ProductListResponse.builder().build();
        Pageable pageable = Pageable.ofSize(10);

        when(productService.getProductsBySeller(sellerId, pageable)).thenReturn(response);

        ResponseEntity<ProductListResponse> result = productController.getProductsBySeller(sellerId, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}