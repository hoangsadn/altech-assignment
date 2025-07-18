package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.dto.Basket;
import com.altech.electronicstore.assignment.dto.Product;
import com.altech.electronicstore.assignment.dto.Receipt;
import com.altech.electronicstore.assignment.dto.ReceiptItem;
import com.altech.electronicstore.assignment.repositories.BasketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private BasketService basketService;

    private Basket basket;
    private Product product;
    private Long userId = 1L;
    private Long productId = 2L;

    @BeforeEach
    void setUp() {
        // Initialize test data
        basket = new Basket();
        basket.setUserId(userId);
        basket.setProducts(new ArrayList<>());

        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setStock(10);
    }

    @Test
    void testAddProduct_Success_NewBasket() {
        // Arrange
        when(basketRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(basketRepository.save(any(Basket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productService.getProductById(productId)).thenReturn(product);
//        when(productService.insertProduct(any(Product.class))).thenReturn(product);

        // Act
        Basket result = basketService.addProduct(userId, productId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(1, result.getProducts().size());
        assertEquals(product, result.getProducts().get(0));
        assertEquals(9, product.getStock()); // Stock decremented by 1
        verify(basketRepository, times(1)).findByUserId(userId);
        verify(basketRepository, times(2)).save(any(Basket.class)); // Once for new basket, once for updated
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void testAddProduct_Success_ExistingBasket() {
        // Arrange
        basket.getProducts().add(new Product()); // Simulate existing product
        when(basketRepository.findByUserId(userId)).thenReturn(Optional.of(basket));
        when(basketRepository.save(any(Basket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productService.getProductById(productId)).thenReturn(product);
//        when(productService.insertProduct(any(Product.class))).thenReturn(product);

        // Act
        Basket result = basketService.addProduct(userId, productId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(2, result.getProducts().size()); // One existing + one new
        assertEquals(product, result.getProducts().get(1));
        assertEquals(9, product.getStock()); // Stock decremented by 1
        verify(basketRepository, times(1)).findByUserId(userId);
        verify(basketRepository, times(1)).save(basket);
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void testAddProduct_ProductOutOfStock() {
        // Arrange
        product.setStock(0);
        when(basketRepository.findByUserId(userId)).thenReturn(Optional.of(basket));
        when(productService.getProductById(productId)).thenReturn(product);

        // Act & Assert
        APIException exception = assertThrows(APIException.class, () -> basketService.addProduct(userId, productId));
        assertEquals(APICode.PRODUCT_OUT_OF_STOCK.getMessage(), exception.getMessage());
        verify(basketRepository, times(1)).findByUserId(userId);
        verify(basketRepository, never()).save(any(Basket.class));
        verify(productService, times(1)).getProductById(productId);
        verify(productService, never()).insertProduct(any(Product.class));
    }

    @Test
    void testGetSingletonBasket_NewBasket() {
        // Arrange
        when(basketRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(basketRepository.save(any(Basket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Basket result = basketService.getSingletonBasket(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertTrue(result.getProducts().isEmpty());
        verify(basketRepository, times(1)).findByUserId(userId);
        verify(basketRepository, times(1)).save(any(Basket.class));
    }

    @Test
    void testGetSingletonBasket_ExistingBasket() {
        // Arrange
        when(basketRepository.findByUserId(userId)).thenReturn(Optional.of(basket));

        // Act
        Basket result = basketService.getSingletonBasket(userId);

        // Assert
        assertNotNull(result);
        assertEquals(basket, result);
        verify(basketRepository, times(1)).findByUserId(userId);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void testRemoveProduct_Success() {
        // Arrange
        basket.getProducts().add(product);
        when(basketRepository.findByUserId(userId)).thenReturn(Optional.of(basket));
        when(basketRepository.save(any(Basket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productService.getProductById(productId)).thenReturn(product);

        // Act
        Basket result = basketService.removeProduct(userId, productId);

        // Assert
        assertNotNull(result);
        assertTrue(result.getProducts().isEmpty());
        assertEquals(11, product.getStock()); // Stock incremented by 1
        verify(basketRepository, times(1)).findByUserId(userId);
        verify(basketRepository, times(1)).save(basket);
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void testRemoveProduct_ProductNotInBasket() {
        // Arrange
        when(basketRepository.findByUserId(userId)).thenReturn(Optional.of(basket));
        when(basketRepository.save(any(Basket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productService.getProductById(productId)).thenReturn(product);

        // Act
        Basket result = basketService.removeProduct(userId, productId);

        // Assert
        assertNotNull(result);
        assertTrue(result.getProducts().isEmpty());
        verify(basketRepository, times(1)).findByUserId(userId);
        verify(basketRepository, times(1)).save(basket);
        verify(productService, times(1)).getProductById(productId);
        verify(productService, never()).insertProduct(any(Product.class));
    }

}