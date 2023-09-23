package com.multitenant.example.tenant.exceptions;

public class DisabledException extends InternalException {

    public DisabledException() {
        super("User disabled");
    }

}
