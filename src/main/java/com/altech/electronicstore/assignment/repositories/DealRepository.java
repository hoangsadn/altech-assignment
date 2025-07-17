package com.altech.electronicstore.assignment.repositories;

import com.altech.electronicstore.assignment.models.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends JpaRepository<Deal, Integer> {

}
