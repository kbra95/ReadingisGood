package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GeneralException {
    public UserNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
