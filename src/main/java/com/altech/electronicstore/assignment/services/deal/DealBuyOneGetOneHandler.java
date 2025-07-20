package com.altech.electronicstore.assignment.services.deal;

import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.common.ValidationUtils;
import com.altech.electronicstore.assignment.entity.Deal;
import com.altech.electronicstore.assignment.dto.Receipt;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("BuyOneGetOne")
public class DealBuyOneGetOneHandler implements DealHandler {

    @Override
    public void validateDeal(Deal deal) {
        LocalDateTime now = LocalDateTime.now();
        ValidationUtils.require(deal.isActive(), () -> new APIException(APICode.DEAL_NOT_VALID));
        ValidationUtils.require(deal.getExpiration() != null && deal.getExpiration().isAfter(now), () -> new APIException(APICode.DEAL_EXPIRED));
    }

    @Override
    public Receipt applyDeal(Receipt receipt) {
        receipt.getItems().forEach(item -> {
            if (item.getQuantity() > 1) {
                int freeItems = item.getQuantity() / 2;
                item.setDiscountedPrice(item.getUnitPrice() * (item.getQuantity() - freeItems));
            }
        });
        return receipt;
    };

}
