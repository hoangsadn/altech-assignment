package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.AbstractIntegrationTest;
import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.dto.Product;
import com.altech.electronicstore.assignment.dto.ProductFilterRequest;
import com.altech.electronicstore.assignment.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
//@Transactional
class ProductServiceIntegrationTest extends AbstractIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceIntegrationTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE product RESTART IDENTITY CASCADE");
    }

    @Test
    void testInsertProduct() {
        Product newProduct = new Product();
        newProduct.setName("New Product Insert");
        newProduct.setPrice(200.0);
        newProduct.setStock(5);
        newProduct.setVersion(0L);

        productService.insertProduct(newProduct);

        assertEquals("New Product Insert", productRepository.findProductByName("New Product Insert").get().getName());
    }

    @Test
    void testFilterProducts() {
        var product = new Product();
        product.setName("Test Service Integration");
        product.setPrice(100.0);
        product.setStock(10);
        product.setCategory("Electronics");
        product.setVersion(0L);
        productRepository.save(product);


        ProductFilterRequest filter = new ProductFilterRequest();
        filter.setPage(0);
        filter.setSize(10);
        filter.setSortBy("name");
        filter.setSortDirection("asc");
        filter.setCategory("Electronics");
        filter.setMinPrice(50.0);
        filter.setMaxPrice(150.0);
        filter.setAvailable(true);
//
        var result = productService.filterProducts(filter);
//        log.info("Filtered products: {}", result.getContent());
        assertEquals(1, result.getContent().size());
        assertEquals("Test Service Integration", result.getContent().get(0).getName());
    }
}