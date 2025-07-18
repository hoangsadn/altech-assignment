package com.altech.electronicstore.assignment.common;


import lombok.Getter;

@Getter
public enum APICode {
    USER_NOT_FOUND(404, "User not found"),
    VALIDATION_FAILED(400, "Validation failed"),

    // out of stock product
    PRODUCT_OUT_OF_STOCK(400, "Product is out of stock"),
    // product not found
    PRODUCT_NOT_FOUND(404, "Product not found")



    ;


    private final int code;
    private final String message;

    APICode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
