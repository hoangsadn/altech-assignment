package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.AbstractTestContainer;
import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.dto.Basket;
import com.altech.electronicstore.assignment.dto.InsertBasketRequest;
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

        InsertBasketRequest request = new InsertBasketRequest(id,
                1,
                1L
        );

        Basket result = basketService.addProduct(request);

        assertEquals(1, result.getBasketProducts().size());
        assertEquals(id, result.getBasketProducts().get(0).getId());
        Product updatedProduct = productRepository.findProductByName(("Test Add Product")).orElseThrow();
        assertEquals(9, updatedProduct.getStock());
        assertEquals(1L, updatedProduct.getVersion());
        log.info("Product added to basket successfully.");
    }

}