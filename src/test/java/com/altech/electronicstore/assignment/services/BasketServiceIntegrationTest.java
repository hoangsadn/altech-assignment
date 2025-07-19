package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.AbstractTestContainer;
import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.dto.Basket;
import com.altech.electronicstore.assignment.dto.Product;
import com.altech.electronicstore.assignment.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
//@Transactional
class BasketServiceIntegrationTest extends AbstractTestContainer {

    private static final Logger log = LoggerFactory.getLogger(BasketServiceIntegrationTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BasketService basketService;

    private Long userId = 5L;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE product RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE basket RESTART IDENTITY CASCADE");

    }



    @Test
    void testAddProduct() {
        var product = new Product();
        product.setName("Test Add Product");
        product.setPrice(100.0);
        product.setStock(10);
        product.setCategory("Electronics");
        product.setVersion(0L);
        var id = productRepository.save(product).getId();

        Basket result = basketService.addProduct(userId, id);

        assertEquals(1, result.getProducts().size());
        assertEquals(id, result.getProducts().get(0).getId());
        Product updatedProduct = productRepository.findProductByName(("Test Add Product")).orElseThrow();
        assertEquals(9, updatedProduct.getStock());
        assertEquals(1L, updatedProduct.getVersion());
        log.info("Product added to basket successfully.");
    }

    @Test
    void testAddProduct_OutOfStock() {
        var product = new Product();
        product.setName("Test Product Service Integration");
        product.setPrice(100.0);
        product.setCategory("Electronics");
        product.setVersion(0L);
        product.setStock(0); // Set stock to 0 to simulate out of stock
        var id = productRepository.save(product).getId();


        assertThrows(APIException.class, () -> basketService.addProduct(userId, id));
    }
//
//    @Test
//    void testGenerateReceipt_WithDiscount() {
//        basket.getProducts().addAll(Arrays.asList(product, product, product));
//        basketRepository.save(basket);
//
//        Receipt result = basketService.generateReceipt(userId);
//
//        assertEquals(1, result.getItems().size());
//        assertEquals(270.0, result.getTotalPrice(), 0.01);
//        assertEquals(1, result.getDealsApplied().size());
//        assertEquals("10% off for 3+ Test Product", result.getDealsApplied().get(0));
//    }
}