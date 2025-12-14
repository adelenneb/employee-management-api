package com.company.employees.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * DTO de réponse d'erreur renvoyé par GlobalExceptionHandler.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final OffsetDateTime timestamp;
    private final int status;
    private final String message;
    private final String path;
    private final Map<String, String> errors;

    public ErrorResponse(OffsetDateTime timestamp, int status, String message, String path, Map<String, String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
        this.errors = errors;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
