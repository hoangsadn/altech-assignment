package com.altech.electronicstore.assignment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Paginated response")
@Entity
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description; // e.g., "Buy 1 get 50% off the second"
    private String code;
    private LocalDateTime expiration;

    private boolean active; // Indicates if the deal is currently active


}