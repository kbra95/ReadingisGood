package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends GeneralException {
    public OrderNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}