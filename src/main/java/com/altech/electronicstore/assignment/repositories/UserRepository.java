package com.altech.electronicstore.assignment.repositories;

import com.altech.electronicstore.assignment.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUsername(String username);

    boolean existsUserByUsername(String username);
}
