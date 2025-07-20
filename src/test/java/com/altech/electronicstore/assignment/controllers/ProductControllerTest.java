package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.dto.Deal;
import com.altech.electronicstore.assignment.dto.Product;
import com.altech.electronicstore.assignment.dto.ProductFilterRequest;
import com.altech.electronicstore.assignment.dto.Response;
import com.altech.electronicstore.assignment.services.ProductService;
import com.altech.electronicstore.assignment.services.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private Deal deal;
    private ProductFilterRequest filterRequest;
    private Page<Product> productPage;

    @BeforeEach
    void setUp() {
        // Initialize test data
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(100.0);

        deal = new Deal();
        deal.setId(1L);

        filterRequest = new ProductFilterRequest();
        // Assume filter has fields like page, size, etc.
        filterRequest.setPage(0);
        filterRequest.setSize(10);

        productPage = new PageImpl<>(Collections.singletonList(product), PageRequest.of(0, 10), 1);
    }

    @Test
    void testCreateProduct_Success() {
        // Arrange
        doNothing().when(productService).insertProduct(any(Product.class));

        // Act
        Response<String> response = productController.createProduct(product);

        // Assert
        assertNotNull(response);
        assertEquals(APICode.PRODUCT_CREATED.getMessage(), response.getData());
        verify(productService, times(1)).insertProduct(product);
    }

    @Test
    void testUpdateProduct_Success() {
        // Arrange
        doNothing().when(productService).updateProduct(any(Product.class));

        // Act
        Response<String> response = productController.updateProduct(product);

        // Assert
        assertNotNull(response);
        assertEquals(APICode.PRODUCT_UPDATED.getMessage(), response.getData());
        verify(productService, times(1)).updateProduct(product);
    }

    @Test
    void testRemoveProduct_Success() {
        // Arrange
        Long productId = 1L;
        doNothing().when(productService).removeProduct(productId);

        // Act
        Response<String> response = productController.removeProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals(APICode.PRODUCT_REMOVED.getMessage(), response.getData());
        verify(productService, times(1)).removeProduct(productId);
    }

    @Test
    void testFilterProducts_Success() {
        // Arrange
        when(productService.filterProducts(any(ProductFilterRequest.class))).thenReturn(productPage);

        // Act
        Response<Page<Product>> response = productController.filterProducts(filterRequest);

        // Assert
        assertNotNull(response);
        assertEquals(productPage, response.getData());
        assertEquals(1, response.getData().getContent().size());
        verify(productService, times(1)).filterProducts(filterRequest);
    }

    @Test
    void testGetProductById_Success() {
        // Arrange
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(product);

        // Act
        Product result = productController.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(product, result);
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void testAddDeal_Success() {
        // Arrange
        Long productId = 1L;
        when(productService.addDealToProduct(productId, deal)).thenReturn(deal);

        // Act
        Deal result = productController.addDeal(productId, deal);

        // Assert
        assertNotNull(result);
        assertEquals(deal, result);
        verify(productService, times(1)).addDealToProduct(productId, deal);
    }


    @Test
    void testCreateProduct_ServiceThrowsException() {
        // Arrange
        doThrow(new RuntimeException("Product creation failed")).when(productService).insertProduct(any(Product.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productController.createProduct(product));
        verify(productService, times(1)).insertProduct(product);
    }

    @Test
    void testGetProductById_NotFound() {
        // Arrange
        Long productId = 1L;
        when(productService.getProductById(productId)).thenThrow(new RuntimeException("Product not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productController.getProductById(productId));
        verify(productService, times(1)).getProductById(productId);
    }
}