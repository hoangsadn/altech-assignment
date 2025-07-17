package com.altech.electronicstore.assignment.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.altech.electronicstore.assignment.services.ProductService;
import com.altech.electronicstore.assignment.models.Product;
import com.altech.electronicstore.assignment.models.Deal;

@RestController
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ping
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @DeleteMapping("/{id}")
    public void removeProduct(@PathVariable Long id) {
        productService.removeProduct(id);
    }

    @GetMapping
    @Operation(summary = "Get paginated list of users")
    @PageableAsQueryParam
    public Page<Product> getProducts(Pageable pageable) {
        return productService.getProducts(pageable);
    }



    @PostMapping("/{id}/deals")
    public Deal addDeal(@PathVariable Long id, @RequestBody Deal deal) {
        return productService.addDealToProduct(id, deal);
    }
}