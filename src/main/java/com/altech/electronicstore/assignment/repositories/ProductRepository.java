package com.altech.electronicstore.assignment.repositories;

import com.altech.electronicstore.assignment.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {



    @Query("""
    SELECT p FROM Product p
    WHERE (:category IS NULL OR p.category = :category)
      AND (:minPrice IS NULL OR p.price >= :minPrice)
      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
      AND (:available IS NULL OR p.stock > 0)
      AND (:searchQuery IS NULL OR
           LOWER(p.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR
           LOWER(p.description) LIKE LOWER(CONCAT('%', :searchQuery, '%')))
    """)
    Page<Product> findByFilters(
            @Param("category") String category,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("available") Boolean available,
            @Param("searchQuery") String searchQuery,
            Pageable pageable
    );


}