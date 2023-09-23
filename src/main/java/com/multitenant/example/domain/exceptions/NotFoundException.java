package com.multitenant.example.domain.exceptions;

public class NotFoundException extends InternalException {

    public NotFoundException(String message) {
        super(message);
    }

}
