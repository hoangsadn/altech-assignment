package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.models.ProductFilterRequest;
import com.altech.electronicstore.assignment.models.Response;
import com.altech.electronicstore.assignment.services.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.altech.electronicstore.assignment.services.ProductService;
import com.altech.electronicstore.assignment.models.Product;
import com.altech.electronicstore.assignment.models.Deal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> createProduct(@RequestBody Product product) {
        productService.insertProduct(product);
        return Response.success("Product created successfully");
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return Response.success("Product updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> removeProduct(@PathVariable Long id) {
        productService.removeProduct(id);
        return Response.success("Product removed successfully");
    }


    @GetMapping
    @Operation(summary = "Get paginated list of users")
    public Response<Page<Product>> filterProducts(ProductFilterRequest filter) {
        Page<Product> products = productService.filterProducts(filter);
        return Response.success(products);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }


    @PostMapping("/{id}/deals")
    public Deal addDeal(@PathVariable Long id, @RequestBody Deal deal) {
        return productService.addDealToProduct(id, deal);
    }


    //TODO: Remove this after testing
    private final RedisService redisService;

    @GetMapping("/flush")
    public String flushCache() {
        redisService.flushAll();
        return "Cache flushed successfully";
    }

}