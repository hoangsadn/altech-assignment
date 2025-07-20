package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.dto.*;
import com.altech.electronicstore.assignment.entity.Basket;
import com.altech.electronicstore.assignment.entity.BasketProduct;
import com.altech.electronicstore.assignment.entity.Deal;
import com.altech.electronicstore.assignment.entity.Product;
import com.altech.electronicstore.assignment.entity.UserProfile;
import com.altech.electronicstore.assignment.repositories.BasketRepository;
import com.altech.electronicstore.assignment.services.deal.DealFactory;
import com.altech.electronicstore.assignment.services.deal.DealHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private ProductService productService;

    @Mock
    private DealFactory dealFactory;

    @Mock
    private DealService dealService;

    @InjectMocks
    private BasketService basketService;

    private Basket basket;
    private Product product;
    private UserProfile user;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Mouse");
        product.setPrice(50.0);
        product.setStock(10);
        product.setBasketProducts(new ArrayList<>());

        basket = new Basket();
        basket.setUserId(1L);
        basket.setBasketProducts(new ArrayList<>());
    }

    @Test
    void testAddProduct_NewProduct_Success() {
        InsertBasketRequest request = new InsertBasketRequest();
        request.setUserId(1L);
        request.setProductId(1L);
        request.setQuantity(2);

        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(basket));
        when(productService.getProductById(1L)).thenReturn(product);
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        Basket result = basketService.addProduct(request);

        assertEquals(1, result.getBasketProducts().size());
        verify(productService).updateProduct(any());
        verify(basketRepository).save(any());
    }

    @Test
    void testAddProduct_AlreadyInBasket_UpdatesQuantity() {
        BasketProduct bp = new BasketProduct();
        bp.setProduct(product);
        bp.setQuantity(2);
        basket.getBasketProducts().add(bp);

        InsertBasketRequest request = new InsertBasketRequest();
        request.setUserId(1L);
        request.setProductId(1L);
        request.setQuantity(1);

        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(basket));
        when(productService.getProductById(1L)).thenReturn(product);
        when(basketRepository.save(any())).thenReturn(basket);

        Basket updated = basketService.addProduct(request);

        assertEquals(1, updated.getBasketProducts().size());
        assertEquals(3, updated.getBasketProducts().get(0).getQuantity());
    }

    @Test
    void testAddProduct_OutOfStock_ThrowsException() {
        InsertBasketRequest request = new InsertBasketRequest();
        request.setUserId(1L);
        request.setProductId(1L);
        request.setQuantity(20); // too much

        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(basket));
        when(productService.getProductById(1L)).thenReturn(product);

        APIException ex = assertThrows(APIException.class, () -> basketService.addProduct(request));
        assertEquals(APICode.PRODUCT_OUT_OF_STOCK.getMessage(), ex.getMessage());
    }

    @Test
    void testRemoveProduct_Success() {
        BasketProduct bp = new BasketProduct();
        bp.setProduct(product);
        bp.setQuantity(3);
        basket.getBasketProducts().add(bp);

        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(basket));
        when(productService.getProductById(1L)).thenReturn(product);
        when(basketRepository.save(any())).thenReturn(basket);

        Basket updated = basketService.removeProduct(1L, 1L);

        assertTrue(updated.getBasketProducts().isEmpty());
        assertEquals(13, product.getStock()); // stock was 10 + 3
    }

    @Test
    void testRemoveProduct_NotFound_ThrowsException() {
        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(basket));
        when(productService.getProductById(1L)).thenReturn(product);

        APIException ex = assertThrows(APIException.class, () -> basketService.removeProduct(1L, 1L));
        assertEquals(APICode.PRODUCT_NOT_FOUND.getMessage(), ex.getMessage());
    }

    @Test
    void testGetSingletonBasket_Exists() {
        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(basket));

        Basket result = basketService.getSingletonBasket(1L);

        assertEquals(1L, result.getUserId());
    }

    @Test
    void testGetSingletonBasket_CreatesNew() {
        when(basketRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(basketRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Basket result = basketService.getSingletonBasket(1L);

        assertEquals(1L, result.getUserId());
        verify(basketRepository).save(any());
    }

    @Test
    void testGenerateReceipt_Success_NoDeals() {
        BasketProduct bp = new BasketProduct();
        bp.setProduct(product);
        bp.setQuantity(2);
        basket.getBasketProducts().add(bp);

        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(basket));

        Receipt receipt = basketService.generateReceipt(1L, "");

        assertEquals(1, receipt.getItems().size());
        assertEquals(100.0, receipt.getTotalPrice()); // 2 * 50
    }

    @Test
    void testApplyDeals_ValidHandler() {
        Receipt receipt = new Receipt();
        receipt.setItems(new ArrayList<>());

        Deal deal = new Deal();
        deal.setCode("SAVE10");

        DealHandler handler = mock(DealHandler.class);

        when(dealService.getDealByCode("SAVE10")).thenReturn(deal);
        when(dealFactory.getHandler("SAVE10")).thenReturn(handler);
        when(handler.applyDeal(receipt)).thenReturn(receipt);

        Receipt result = basketService.applyDeals("SAVE10", receipt);

        verify(handler).applyDeal(receipt);
        assertSame(receipt, result);
    }

    @Test
    void testApplyDeals_InvalidCode_Ignored() {
        Receipt receipt = new Receipt();
        receipt.setItems(new ArrayList<>());

        when(dealService.getDealByCode("INVALID")).thenReturn(null);

        Receipt result = basketService.applyDeals("INVALID", receipt);

        assertSame(receipt, result);
    }
}
