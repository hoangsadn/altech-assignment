package com.altech.electronicstore.assignment.dto;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Version
    private Long version; // Optimistic locking field

    private String name;
    private String description;
    private String imageUrl;
    private int stock;
    private double price;
    private String category;
}
