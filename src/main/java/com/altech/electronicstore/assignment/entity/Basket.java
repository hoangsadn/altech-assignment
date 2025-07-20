package com.altech.electronicstore.assignment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Version
    private Long version;

    // Comma-separated list of deal codes applied to the basket
    private String codesApplied;


    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasketProduct> basketProducts = new ArrayList<>();

}