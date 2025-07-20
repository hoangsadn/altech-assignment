package com.altech.electronicstore.assignment.repositories;

import com.altech.electronicstore.assignment.dto.Deal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    Optional<Deal> findByCode(String code);

    @Query("""
    SELECT d FROM Deal d
    WHERE (:active IS NULL OR d.active = :active)
      AND (:searchQuery IS NULL OR (
            LOWER(d.code) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR
            LOWER(d.description) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
          )
      )
    """)
    Page<Deal> findByFilters(
            @Param("active") Boolean active,
            @Param("searchQuery") String searchQuery,
            Pageable pageable
    );
}
