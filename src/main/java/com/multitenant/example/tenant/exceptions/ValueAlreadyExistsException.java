package com.multitenant.example.tenant.exceptions;

public class ValueAlreadyExistsException extends InternalException {

    public ValueAlreadyExistsException(String message) {
        super(message);
    }

}
