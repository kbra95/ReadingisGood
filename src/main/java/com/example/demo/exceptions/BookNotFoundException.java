package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends RuntimeException{
    private final HttpStatus httpStatus;

    public BookNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

}
