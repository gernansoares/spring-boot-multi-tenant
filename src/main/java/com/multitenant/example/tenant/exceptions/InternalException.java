package com.multitenant.example.tenant.exceptions;

public class InternalException extends RuntimeException {

    InternalException(String message) {
        super(message);
    }

}
