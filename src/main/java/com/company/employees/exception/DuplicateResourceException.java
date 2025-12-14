package com.company.employees.exception;

/**
 * Exception levée lorsqu'une ressource existe déjà (par exemple : email dupliqué).
 */
public class DuplicateResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String message) {
        super(message);
    }
}
