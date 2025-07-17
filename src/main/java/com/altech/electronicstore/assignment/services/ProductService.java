package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.models.Deal;
import com.altech.electronicstore.assignment.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.altech.electronicstore.assignment.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // This service would contain methods to handle product-related operations
    // such as creating, deleting, and retrieving products.

    // Example method to create a product
    public Product createProduct(Product product) {
        // Logic to save the product to the database
        return productRepository.save(product); // Assuming productRepository is a JPA repository
    }

    // Example method to remove a product by ID
    public void removeProduct(Long id) {
        productRepository.deleteById(id); // Assuming productRepository is a JPA repository
    }

    // Example method to get products with pagination
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Example method to add a deal to a product
    public Deal addDealToProduct(Long id, Deal deal) {
        return deal; // Return the updated deal
    }

}