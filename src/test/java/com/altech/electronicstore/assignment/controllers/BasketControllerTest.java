package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.auth.AuthUtil;
import com.altech.electronicstore.assignment.dto.*;
import com.altech.electronicstore.assignment.services.BasketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketControllerTest {

    @Mock
    private AuthUtil authUtil;

    @Mock
    private BasketService basketService;

    @InjectMocks
    private BasketController basketController;

    private UserProfile mockUser;
    private Basket mockBasket;
    private Receipt mockReceipt;

    @BeforeEach
    void setUp() {
        mockUser = new UserProfile();
        mockUser.setId(1L);

        mockBasket = new Basket();
        mockBasket.setUserId(1L);
        mockBasket.setBasketProducts(Collections.emptyList());

        mockReceipt = new Receipt();
        mockReceipt.setItems(Collections.emptyList());

        when(authUtil.getUserInfo()).thenReturn(mockUser);
    }

    @Test
    void testAddProductToBasket_Success() {
        // Arrange
        InsertBasketRequest request = new InsertBasketRequest();
        request.setProductId(10L);
        request.setQuantity(2);

        when(basketService.addProduct(any(InsertBasketRequest.class))).thenReturn(mockBasket);

        // Act
        Response<Basket> response = basketController.addProductToBasket(request);

        // Assert
        assertNotNull(response);
        assertEquals(mockBasket, response.getData());
        verify(basketService).addProduct(any(InsertBasketRequest.class));
    }

    @Test
    void testRemoveProductFromBasket_Success() {
        // Arrange
        Long productId = 10L;
        when(basketService.removeProduct(mockUser.getId(), productId)).thenReturn(mockBasket);

        // Act
        Response<Basket> response = basketController.removeProductFromBasket(productId);

        // Assert
        assertNotNull(response);
        assertEquals(mockBasket, response.getData());
        verify(basketService).removeProduct(mockUser.getId(), productId);
    }

    @Test
    void testGetBasket_Success() {
        // Arrange
        when(basketService.getSingletonBasket(mockUser.getId())).thenReturn(mockBasket);

        // Act
        Response<Basket> response = basketController.getBasket();

        // Assert
        assertNotNull(response);
        assertEquals(mockBasket, response.getData());
        verify(basketService).getSingletonBasket(mockUser.getId());
    }

    @Test
    void testGetReceipt_Success() {
        // Arrange
        String code = "DISCOUNT10";
        when(basketService.generateReceipt(mockUser.getId(), code)).thenReturn(mockReceipt);

        // Act
        Response<Receipt> response = basketController.getReceipt(code);

        // Assert
        assertNotNull(response);
        assertEquals(mockReceipt, response.getData());
        verify(basketService).generateReceipt(mockUser.getId(), code);
    }

    @Test
    void testAddProductToBasket_ExceptionThrown() {
        // Arrange
        InsertBasketRequest request = new InsertBasketRequest();
        request.setProductId(10L);
        request.setQuantity(2);

        when(basketService.addProduct(any())).thenThrow(new RuntimeException("Add failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> basketController.addProductToBasket(request));
        verify(basketService).addProduct(any());
    }

    @Test
    void testRemoveProductFromBasket_NotFound() {
        // Arrange
        Long productId = 999L;
        when(basketService.removeProduct(mockUser.getId(), productId))
                .thenThrow(new RuntimeException("Product not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> basketController.removeProductFromBasket(productId));
        verify(basketService).removeProduct(mockUser.getId(), productId);
    }
}
