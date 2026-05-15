package com.atlasys.freeplanning.identity.exception;

import com.atlasys.freeplanning.infra.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class IdentityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> usernameNotFound(
            UsernameNotFoundException exception,
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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> usernameNotFound(
            BadCredentialsException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(new ErrorResponse(
                status.value(),
                status.name(),
                "Invalid email or password provided",
                request.getRequestURI(),
                Instant.now()
        ));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> invalidToken(
            InvalidTokenException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(new ErrorResponse(
                status.value(),
                status.name(),
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now()
        ));
    }

    @ExceptionHandler(EntityChangeFailureException.class)
    public ResponseEntity<ErrorResponse> entityChangeFailure(
            EntityChangeFailureException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(new ErrorResponse(
                status.value(),
                status.name(),
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now()
        ));
    }
}
