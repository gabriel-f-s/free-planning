package com.atlasys.freeplanning.infra.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFound(
            EntityNotFoundException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorResponse(
                status.value(),
                status.name(),
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now()
        ));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> entityExists(
            EntityExistsException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponse(
                status.value(),
                status.name(),
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now()
        ));
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ErrorResponse> optimisticLockFailure(
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponse(
                status.value(),
                status.name(),
                "The record was updated by another transaction. Please refresh and try again",
                request.getRequestURI(),
                Instant.now()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> dataIntegrityViolationFailure(
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponse(
                status.value(),
                status.name(),
                "A record with this unique identifier already exists",
                request.getRequestURI(),
                Instant.now()
        ));
    }
}
