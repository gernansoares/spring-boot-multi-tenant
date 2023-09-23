package com.multitenant.example.domain.exceptions;

public class ValueAlreadyExistsException extends InternalException {

    public ValueAlreadyExistsException(String message) {
        super(message);
    }

}
