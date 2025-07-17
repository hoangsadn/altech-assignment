package com.altech.electronicstore.assignment.repositories;

import com.altech.electronicstore.assignment.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Additional query methods if needed
    Page<Product> findAll(Pageable pageable);

    // Custom query with pagination
//    @Query("SELECT u FROM User u WHERE u.active = true")
//    Page<Product> findActiveUsers(Pageable pageable);
}