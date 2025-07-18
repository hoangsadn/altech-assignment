package com.altech.electronicstore.assignment.common;


import lombok.Getter;

@Getter
public enum APICode {
    USER_NOT_FOUND(404, "User not found"),

    VALIDATION_FAILED(400, "Validation failed"),

    PRODUCT_NOT_FOUND(500, "Product not found"),
    PRODUCT_OUT_OF_STOCK(51, "Product is out of stock"),

    //Product created successfully
    PRODUCT_CREATED(501, "Product created successfully"),
    PRODUCT_UPDATED(502, "Product updated successfully"),
    PRODUCT_REMOVED(503, "Product removed successfully")


    ;


    private final int code;
    private final String message;

    APICode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
