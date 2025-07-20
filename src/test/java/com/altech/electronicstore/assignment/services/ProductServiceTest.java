package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.dto.Deal;
import com.altech.electronicstore.assignment.dto.Product;
import com.altech.electronicstore.assignment.dto.ProductFilterRequest;
import com.altech.electronicstore.assignment.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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
        filterRequest.setPage(0);
        filterRequest.setSize(10);
        filterRequest.setSortBy("name");
        filterRequest.setSortDirection("asc");
        filterRequest.setCategory("Electronics");
        filterRequest.setMinPrice(50.0);
        filterRequest.setMaxPrice(150.0);
        filterRequest.setAvailable(true);
        filterRequest.setSearchQuery("test");

        productPage = new PageImpl<>(Collections.singletonList(product), PageRequest.of(0, 10), 1);
    }

    @Test
    void testInsertProduct_Success() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        productService.insertProduct(product);

        // Assert
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_Success() {
        // Arrange
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        productService.updateProduct(product);

        // Assert
        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        // Arrange
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        // Act & Assert
        APIException exception = assertThrows(APIException.class, () -> productService.updateProduct(product));
        assertEquals(APICode.PRODUCT_OUT_OF_STOCK.getMessage(), exception.getMessage());
        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testRemoveProduct_Success() {
        // Arrange
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(product.getId());

        // Act
        productService.removeProduct(product.getId());

        // Assert
        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, times(1)).deleteById(product.getId());
    }

    @Test
    void testRemoveProduct_ProductNotFound() {
        // Arrange
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        // Act & Assert
        APIException exception = assertThrows(APIException.class, () -> productService.removeProduct(product.getId()));
        assertEquals(APICode.PRODUCT_OUT_OF_STOCK.getMessage(), exception.getMessage());
        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void testAddDealToProduct_Todo() {
        // Arrange
        Long productId = 1L;
        // Note: The method is currently a placeholder, returning the input deal directly

        // Act
        Deal result = productService.addDealToProduct(productId, deal);

        // Assert
        assertEquals(deal, result);
        // No repository interaction to verify, as the method is not implemented
    }

    @Test
    void testGetProductById_Success() {
        // Arrange
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(product.getId());

        // Assert
        assertNotNull(result);
        assertEquals(product, result);
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    void testGetProductById_ProductNotFound() {
        // Arrange
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        // Act & Assert
        APIException exception = assertThrows(APIException.class, () -> productService.getProductById(product.getId()));
        assertEquals(APICode.PRODUCT_OUT_OF_STOCK.getMessage(), exception.getMessage());
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    void testFilterProducts_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(productRepository.findByFilters(
                filterRequest.getCategory(),
                filterRequest.getMinPrice(),
                filterRequest.getMaxPrice(),
                filterRequest.getAvailable(),
                filterRequest.getSearchQuery(),
                pageable
        )).thenReturn(productPage);

        // Act
        Page<Product> result = productService.filterProducts(filterRequest);

        // Assert
        assertNotNull(result);
        assertEquals(productPage, result);
        assertEquals(1, result.getContent().size());
        verify(productRepository, times(1)).findByFilters(
                filterRequest.getCategory(),
                filterRequest.getMinPrice(),
                filterRequest.getMaxPrice(),
                filterRequest.getAvailable(),
                filterRequest.getSearchQuery(),
                pageable
        );
    }

    @Test
    void testFilterProducts_WithDescendingSort() {
        // Arrange
        filterRequest.setSortDirection("desc");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").descending());
        when(productRepository.findByFilters(
                filterRequest.getCategory(),
                filterRequest.getMinPrice(),
                filterRequest.getMaxPrice(),
                filterRequest.getAvailable(),
                filterRequest.getSearchQuery(),
                pageable
        )).thenReturn(productPage);

        // Act
        Page<Product> result = productService.filterProducts(filterRequest);

        // Assert
        assertNotNull(result);
        assertEquals(productPage, result);
        verify(productRepository, times(1)).findByFilters(
                filterRequest.getCategory(),
                filterRequest.getMinPrice(),
                filterRequest.getMaxPrice(),
                filterRequest.getAvailable(),
                filterRequest.getSearchQuery(),
                pageable
        );
    }

    @Test
    void testFilterProducts_WithDefaultPageAndSize() {
        // Arrange
        filterRequest.setPage(null);
        filterRequest.setSize(null);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(productRepository.findByFilters(
                filterRequest.getCategory(),
                filterRequest.getMinPrice(),
                filterRequest.getMaxPrice(),
                filterRequest.getAvailable(),
                filterRequest.getSearchQuery(),
                pageable
        )).thenReturn(productPage);

        // Act
        Page<Product> result = productService.filterProducts(filterRequest);

        // Assert
        assertNotNull(result);
        assertEquals(productPage, result);
        verify(productRepository, times(1)).findByFilters(
                filterRequest.getCategory(),
                filterRequest.getMinPrice(),
                filterRequest.getMaxPrice(),
                filterRequest.getAvailable(),
                filterRequest.getSearchQuery(),
                pageable
        );
    }
}