package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.dto.*;
import com.altech.electronicstore.assignment.repositories.BasketRepository;
import com.altech.electronicstore.assignment.services.deal.DealFactory;
import com.altech.electronicstore.assignment.services.deal.DealHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasketServiceTest {

    @InjectMocks
    private BasketService basketService;

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private ProductService productService;

    @Mock
    private DealFactory dealFactory;

    @Mock
    private DealService dealService;

    private Basket mockBasket;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setStock(10);
        mockProduct.setPrice(100.0);
        mockProduct.setName("Test Product");
        mockProduct.setBasketProducts(new ArrayList<>());

        mockBasket = new Basket();
        mockBasket.setUserId(1L);
        mockBasket.setBasketProducts(new ArrayList<>());
    }

    @Test
    void testAddProduct_NewProduct_Success() {
        InsertBasketRequest request = new InsertBasketRequest();
        request.setProductId(1L);
        request.setUserId(1L);
        request.setQuantity(2);

        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(mockBasket));
        when(productService.getProductById(1L)).thenReturn(mockProduct);
        when(basketRepository.save(any(Basket.class))).thenReturn(mockBasket);

        Basket result = basketService.addProduct(request);

        assertEquals(1, result.getBasketProducts().size());
        verify(productService).updateProduct(any());
        verify(basketRepository).save(any());
    }

    @Test
    void testAddProduct_OutOfStock_ThrowsException() {
        InsertBasketRequest request = new InsertBasketRequest();
        request.setProductId(1L);
        request.setUserId(1L);
        request.setQuantity(20); // Exceeds stock

        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(mockBasket));
        when(productService.getProductById(1L)).thenReturn(mockProduct);

        APIException exception = assertThrows(APIException.class, () -> basketService.addProduct(request));
        assertEquals(APICode.PRODUCT_OUT_OF_STOCK.getMessage(), exception.getMessage());
    }

    @Test
    void testRemoveProduct_Success() {
        BasketProduct bp = new BasketProduct();
        bp.setProduct(mockProduct);
        bp.setQuantity(2);
        mockBasket.getBasketProducts().add(bp);

        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(mockBasket));
        when(productService.getProductById(1L)).thenReturn(mockProduct);
        when(basketRepository.save(any())).thenReturn(mockBasket);

        Basket updated = basketService.removeProduct(1L, 1L);

        assertTrue(updated.getBasketProducts().isEmpty());
        assertEquals(12, mockProduct.getStock()); // 10 + 2
    }

    @Test
    void testRemoveProduct_NotInBasket_ThrowsException() {
        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(mockBasket));
        when(productService.getProductById(1L)).thenReturn(mockProduct);

        APIException exception = assertThrows(APIException.class, () -> basketService.removeProduct(1L, 1L));
        assertEquals(APICode.PRODUCT_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void testGenerateReceipt_NoDeals() {
        BasketProduct bp = new BasketProduct();
        bp.setProduct(mockProduct);
        bp.setQuantity(2);
        mockBasket.getBasketProducts().add(bp);

        when(basketRepository.findByUserId(1L)).thenReturn(Optional.of(mockBasket));

        Receipt receipt = basketService.generateReceipt(1L, "");

        assertEquals(1, receipt.getItems().size());
        assertEquals(200.0, receipt.getTotalPrice());
    }

    @Test
    void testApplyDeals_ValidDealApplied() {
        Receipt receipt = new Receipt();
        receipt.setItems(new ArrayList<>());

        Deal mockDeal = new Deal();
        mockDeal.setCode("SAVE10");

        DealHandler mockHandler = mock(DealHandler.class);
        when(dealService.getDealByCode("SAVE10")).thenReturn(mockDeal);
        when(dealFactory.getHandler("SAVE10")).thenReturn(mockHandler);
        when(mockHandler.applyDeal(receipt)).thenReturn(receipt);

        Receipt updated = basketService.applyDeals("SAVE10", receipt);

        assertSame(receipt, updated);
        verify(mockHandler).applyDeal(receipt);
    }

    @Test
    void testApplyDeals_DealCodeNotFound() {
        Receipt receipt = new Receipt();
        receipt.setItems(new ArrayList<>());

        when(dealService.getDealByCode("INVALID")).thenReturn(null);

        Receipt result = basketService.applyDeals("INVALID", receipt);

        assertSame(receipt, result); // unchanged
    }

    @Test
    void testGetSingletonBasket_CreatesNewIfNotExist() {
        when(basketRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(basketRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Basket result = basketService.getSingletonBasket(1L);

        assertEquals(1L, result.getUserId());
        verify(basketRepository).save(any());
    }
}
