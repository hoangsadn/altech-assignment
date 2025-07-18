package com.altech.electronicstore.assignment.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Data
@AllArgsConstructor
public class APIException extends RuntimeException {
    private final int code;
    private final String message;

    public APIException(APICode apiCode) {
        this.code = apiCode.getCode();
        this.message = apiCode.getMessage();
    }
}