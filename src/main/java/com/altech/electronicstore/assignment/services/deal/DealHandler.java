package com.altech.electronicstore.assignment.services.deal;

import com.altech.electronicstore.assignment.entity.Deal;
import com.altech.electronicstore.assignment.dto.Receipt;

public interface DealHandler {
    void validateDeal(Deal deal);

    Receipt applyDeal(Receipt receipt);

}
