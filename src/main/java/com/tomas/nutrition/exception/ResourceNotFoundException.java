package com.tomas.nutrition.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String recurso, Long id) {
        super(recurso + " com id " + id + " nao foi encontrado.");
    }
}