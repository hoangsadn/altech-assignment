package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Response<Void>> handleApiException(APIException ex) {
        Response<Void> errorResponse = Response.error(ex.getCode(), ex.getMessage());
        HttpStatus status = HttpStatus.resolve(ex.getCode());
        return new ResponseEntity<>(errorResponse, status != null ? status : HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGenericException(Exception ex) {
        Response<Void> errorResponse = Response.error(500, "Internal Server Error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}