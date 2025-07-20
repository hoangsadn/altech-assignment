package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.dto.ProductFilterRequest;
import com.altech.electronicstore.assignment.dto.Response;
import com.altech.electronicstore.assignment.services.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.altech.electronicstore.assignment.services.ProductService;
import com.altech.electronicstore.assignment.entity.Product;
import com.altech.electronicstore.assignment.entity.Deal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Endpoints for managing products and product deals")
public class ProductController {

    private final ProductService productService;


    @Operation(
        summary = "Create a new product. Only accessible by admin users ",
        description = "Creates a new product. Only accessible by admin users."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product created successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access."),
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins can create products.")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> createProduct(@RequestBody Product product) {
        productService.insertProduct(product);
        return Response.success(APICode.PRODUCT_CREATED.getMessage());
    }

    @Operation(
        summary = "Update an existing product. Only accessible by admin users",
        description = "Updates the details of an existing product. Only accessible by admin users."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access."),
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins can update products.")
    })
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return Response.success(APICode.PRODUCT_UPDATED.getMessage());
    }

    @Operation(
        summary = "Remove a product. Only accessible by admin users",
        description = "Removes a product by its ID. Only accessible by admin users."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product removed successfully."),
        @ApiResponse(responseCode = "404", description = "Product not found."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access."),
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins can remove products.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> removeProduct(@PathVariable Long id) {
        productService.removeProduct(id);
        return Response.success(APICode.PRODUCT_REMOVED.getMessage());
    }


    @Operation(
        summary = "Filter products",
        description = "Retrieves a paginated list of products based on filter criteria."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully."),
        @ApiResponse(responseCode = "400", description = "Invalid filter parameters.")
    })
    @GetMapping
    public Response<Page<Product>> filterProducts(ProductFilterRequest filter) {
        Page<Product> products = productService.filterProducts(filter);
        return Response.success(products);
    }

    @Operation(
        summary = "Get product by ID",
        description = "Retrieves a product by its unique ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product retrieved successfully."),
        @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }


    @Operation(
        summary = "Add a deal to a product",
        description = "Adds a promotional deal to a product by its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deal added successfully."),
        @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @PostMapping("/{id}/deals")
    public Deal addDeal(@PathVariable Long id, @RequestBody Deal deal) {
        return productService.addDealToProduct(id, deal);
    }


    //TODO: Remove this after testing
    private final RedisService redisService;

    @Operation(
        summary = "Flush Redis cache",
        description = "Flushes all data from Redis cache. For testing purposes only."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cache flushed successfully.")
    })
    @GetMapping("/flush")
    public String flushCache() {
        redisService.flushAll();
        return "Cache flushed successfully";
    }

}