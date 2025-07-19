package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.auth.AuthUtil;
import com.altech.electronicstore.assignment.dto.Basket;
import com.altech.electronicstore.assignment.dto.InsertBasketRequest;
import com.altech.electronicstore.assignment.dto.Receipt;
import com.altech.electronicstore.assignment.dto.Response;
import com.altech.electronicstore.assignment.dto.UserProfile;
import com.altech.electronicstore.assignment.services.BasketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private UserProfile userProfile;
    private Basket basket;
    private Receipt receipt;
    private InsertBasketRequest insertBasketRequest;

    @BeforeEach
    void setUp() {
        // Initialize test data
        userProfile = new UserProfile();
        userProfile.setId(1L);

        basket = new Basket();
        // Assume Basket has some fields, e.g., userId, productIds
        basket.setUserId(1L);

        receipt = new Receipt();
        // Assume Receipt has some fields, e.g., totalPrice
        receipt.setTotalPrice(100.0);

        insertBasketRequest = new InsertBasketRequest();
        insertBasketRequest.setProductId(2L);
    }

    @Test
    void testAddProductToBasket_Success() {
        // Arrange
        when(authUtil.getUserInfo()).thenReturn(userProfile);
        when(basketService.addProduct(userProfile.getId(), insertBasketRequest.getProductId())).thenReturn(basket);

        // Act
        Response<Basket> response = basketController.addProductToBasket(insertBasketRequest);

        // Assert
        assertNotNull(response);
        assertEquals(basket, response.getData());
        verify(authUtil, times(1)).getUserInfo();
        verify(basketService, times(1)).addProduct(userProfile.getId(), insertBasketRequest.getProductId());
    }

    @Test
    void testRemoveProductFromBasket_Success() {
        // Arrange
        Long productId = 2L;
        when(authUtil.getUserInfo()).thenReturn(userProfile);
        when(basketService.removeProduct(userProfile.getId(), productId)).thenReturn(basket);

        // Act
        Response<Basket> response = basketController.removeProductFromBasket(productId);

        // Assert
        assertNotNull(response);
        assertEquals(basket, response.getData());
        verify(authUtil, times(1)).getUserInfo();
        verify(basketService, times(1)).removeProduct(userProfile.getId(), productId);
    }

    @Test
    void testGetBasket_Success() {
        // Arrange
        when(authUtil.getUserInfo()).thenReturn(userProfile);
        when(basketService.getSingletonBasket(userProfile.getId())).thenReturn(basket);

        // Act
        Response<Basket> response = basketController.getBasket();

        // Assert
        assertNotNull(response);
        assertEquals(basket, response.getData());
        verify(authUtil, times(1)).getUserInfo();
        verify(basketService, times(1)).getSingletonBasket(userProfile.getId());
    }

    @Test
    void testGetReceipt_Success() {
        // Arrange
        when(authUtil.getUserInfo()).thenReturn(userProfile);
        when(basketService.generateReceipt(userProfile.getId())).thenReturn(receipt);

        // Act
        Response<Receipt> response = basketController.getReceipt();

        // Assert
        assertNotNull(response);
        assertEquals(receipt, response.getData());
        verify(authUtil, times(1)).getUserInfo();
        verify(basketService, times(1)).generateReceipt(userProfile.getId());
    }

    @Test
    void testAddProductToBasket_AuthUtilThrowsException() {
        // Arrange
        when(authUtil.getUserInfo()).thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> basketController.addProductToBasket(insertBasketRequest));
        verify(authUtil, times(1)).getUserInfo();
        verify(basketService, never()).addProduct(anyLong(), anyLong());
    }

    @Test
    void testRemoveProductFromBasket_ServiceThrowsException() {
        // Arrange
        Long productId = 2L;
        when(authUtil.getUserInfo()).thenReturn(userProfile);
        when(basketService.removeProduct(userProfile.getId(), productId))
                .thenThrow(new RuntimeException("Product not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> basketController.removeProductFromBasket(productId));
        verify(authUtil, times(1)).getUserInfo();
        verify(basketService, times(1)).removeProduct(userProfile.getId(), productId);
    }
}