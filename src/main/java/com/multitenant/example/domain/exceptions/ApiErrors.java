package com.multitenant.example.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Data
public class ApiErrors {

    private List<String> erros;

    public ApiErrors(String message) {
        this.erros = Arrays.asList(message);
    }

}
