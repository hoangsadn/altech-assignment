package com.altech.electronicstore.assignment.services.deal;

import com.altech.electronicstore.assignment.dto.Deal;
import com.altech.electronicstore.assignment.dto.Receipt;
import org.springframework.stereotype.Component;

@Component("TEN_PERCENT_OFF")
public class DealFullHandler implements DealHandler {
    @Override
    public void validateDeal(Deal deal) {

    }

    @Override
    public Receipt applyDeal(Receipt receipt) {
        receipt.getItems().forEach(item -> {
            double discount = item.getUnitPrice() * 0.10 * item.getQuantity(); // 10% discount
            item.setDiscountedPrice(discount);
        });

        return receipt;
    }

}
