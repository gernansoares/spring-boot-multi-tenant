package com.multitenant.example.domain.exceptions;

public class InternalException extends RuntimeException {

    InternalException(String message) {
        super(message);
    }

}
