package com.multitenant.example.tenant.config.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiErrors {

    private List<String> errors;

    public ApiErrors(String message) {
        this.errors = Arrays.asList(message);
    }

}
