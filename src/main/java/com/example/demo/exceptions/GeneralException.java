package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException{
    private final HttpStatus httpStatus;

    public GeneralException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }


}