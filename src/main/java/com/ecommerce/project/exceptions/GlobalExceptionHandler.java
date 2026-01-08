package com.ecommerce.project.exceptions;

import com.ecommerce.project.payload.APIResponse;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.constraintvalidators.hv.ParameterScriptAssertValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String,String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err->{
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName,message);
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Map<String,String>> handleTransactionException(TransactionSystemException ex){
        Throwable cause = ex.getRootCause();
        if(cause instanceof ConstraintViolationException violationException){
            Map<String,String> errors = new HashMap<>();
            violationException.getConstraintViolations()
                    .forEach(violation -> {
                        String field = violation.getPropertyPath().toString();
                        String message = violation.getMessage();
                        errors.put(field,message);
                    });
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                Map.of("error","Transaction failed"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        APIResponse response = new APIResponse(message,false);
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e){
        String message = e.getMessage();
        APIResponse response = new APIResponse(message,false);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }




}
