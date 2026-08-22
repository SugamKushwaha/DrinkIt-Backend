package com.drinkIt.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================
    // VALIDATION ERRORS
    // ==========================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }


    // ==========================================
    // EMAIL ALREADY EXISTS
    // ==========================================

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        errors.put(
                "email",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errors);
    }


    // ==========================================
    // DATABASE CONSTRAINT ERROR
    // ==========================================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        String message = ex.getMostSpecificCause()
                .getMessage();

        if (message != null &&
                message.toLowerCase().contains("email")) {

            errors.put(
                    "email",
                    "Email is already registered"
            );

        } else {

            errors.put(
                    "general",
                    "This information is already registered"
            );
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errors);
    }
      @ExceptionHandler(
            RuntimeException.class
    )
    public ResponseEntity<Map<String, String>>
    handleRuntimeException(
            RuntimeException ex
    ) {

        Map<String, String> errors =
                new HashMap<>();

        errors.put(
                "general",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
 }
}