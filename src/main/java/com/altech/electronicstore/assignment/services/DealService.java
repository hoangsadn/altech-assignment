package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.models.Deal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.altech.electronicstore.assignment.repositories.DealRepository;

@Service
public class DealService {
    // This class will handle business logic related to deals
    // It can include methods for creating, updating, deleting, and retrieving deals
    // For example:

    @Autowired
    private DealRepository dealRepository; // Assuming DealRepository is a JPA repository

    public void createDeal(Deal deal) {
        dealRepository.save(deal);
//        System.out.println("Deal created with details: " + dealDetails);
    }

    public void updateDeal(int dealId, String updatedDetails) {
        dealRepository.save(dealRepository.findById(dealId).get());
    }

    public void deleteDeal(int dealId) {
        dealRepository.deleteById(dealId);
    }

    public String getDeal(int dealId) {
        Deal deal = dealRepository.findById(dealId).orElse(null);
        if (deal != null) {
            return deal.toString(); // Assuming Deal has a proper toString method
        } else {
            return "Deal not found";
        }
    }
}
