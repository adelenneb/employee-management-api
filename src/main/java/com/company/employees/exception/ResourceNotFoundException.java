package com.company.employees.exception;

/**
 * Exception levée lorsque la ressource demandée n'existe pas.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
