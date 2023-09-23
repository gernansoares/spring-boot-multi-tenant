package com.multitenant.example.tenant.exceptions;

public class NotFoundException extends InternalException {

    public NotFoundException(String message) {
        super(message);
    }

}
