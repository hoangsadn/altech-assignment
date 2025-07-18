package com.altech.electronicstore.assignment.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Getter
@Jacksonized
public class Response<T> extends BaseResponse {
    private T data;

    public static <T> Response<T> success(T data) {
        return Response.<T>builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> Response<T> error(int code, String message) {
        return Response.<T>builder()
                .code(code)
                .message(message)
                .build();
    }
}