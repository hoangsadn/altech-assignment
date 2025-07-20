package com.altech.electronicstore.assignment.common;


import lombok.Getter;

@Getter
public enum APICode {
    USER_NOT_FOUND(404, "User not found"),

    VALIDATION_FAILED(400, "Validation failed"),

    PRODUCT_NOT_FOUND(500, "Product not found"),
    PRODUCT_OUT_OF_STOCK(501, "Product is out of stock"),

    //Product created successfully
    PRODUCT_CREATED(501, "Product created successfully"),
    PRODUCT_UPDATED(502, "Product updated successfully"),
    PRODUCT_REMOVED(503, "Product removed successfully")


    // Basket related codes
    , BASKET_NOT_FOUND(600, "Basket not found"),
    BASKET_CREATED(601, "Basket created successfully"),
    BASKET_UPDATED(602, "Basket updated successfully"),
    BASKET_REMOVED(603, "Basket removed successfully"),
    BASKET_PRODUCT_ADDED(604, "Product added to basket successfully"),
    BASKET_PRODUCT_REMOVED(605, "Product removed from basket successfully"),
    // get
    BASKET_GET(606, "Basket retrieved successfully"),

    // Deal related codes
    DEAL_NOT_FOUND(650, "Deal not found"),
    DEAL_NOT_VALID(651, "Deal is not valid"),
    // Deal exprited
    DEAL_EXPIRED(652, "Deal has expired"),
    DEAL_CREATED(651, "Deal created successfully"),
    DEAL_UPDATED(652, "Deal updated successfully"),
    DEAL_REMOVED(653, "Deal removed successfully"),
    DEAL_APPLIED(654, "Deal applied successfully"),



    // Receipt related codes
    RECEIPT_NOT_FOUND(700, "Receipt not found"),
    RECEIPT_CREATED(701, "Receipt created successfully"),
    RECEIPT_UPDATED(702, "Receipt updated successfully"),


    ;



    private final int code;
    private final String message;

    APICode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
