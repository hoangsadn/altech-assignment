package com.altech.electronicstore.assignment.common;

import java.util.function.Supplier;

public class ValidationUtils {

    public static void require(boolean condition, Supplier<APIException> elseThrow) {
        if (!condition) {
            throw elseThrow.get();
        }
    }
}
