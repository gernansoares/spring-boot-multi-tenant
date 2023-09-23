package com.multitenant.example.tenant.config.exceptions;

import com.multitenant.example.tenant.exceptions.ApiErrors;
import com.multitenant.example.tenant.exceptions.InternalException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationErrors(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());
        BindingResult result = exception.getBindingResult();
        List<String> mensagens = result.getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErrors(mensagens);
    }

    @ExceptionHandler(InternalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleErrors(InternalException exception) {
        log.error(exception.getMessage());
        String mensagemErro = exception.getMessage();
        return ResponseEntity.badRequest().body(mensagemErro);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleErrors(HttpRequestMethodNotSupportedException exception) {
        log.error(exception.getMessage());
        String mensagemErro = exception.getMessage();
        return ResponseEntity.badRequest().body(mensagemErro);
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity handleErrors(JwtException exception) {
        log.error(exception.getMessage());
        String mensagemErro = exception.getMessage();
        return ResponseEntity.badRequest().body(mensagemErro);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity handleErrors(Exception exception) {
        log.error(exception.getMessage());
        exception.printStackTrace();
        return ResponseEntity.badRequest().body("Internal error, contact support");
    }

}
