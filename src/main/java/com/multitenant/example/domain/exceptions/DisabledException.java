package com.multitenant.example.domain.exceptions;

public class DisabledException extends InternalException {

    public DisabledException() {
        super("User disabled");
    }

}
