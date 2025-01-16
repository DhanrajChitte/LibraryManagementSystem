package com.LibraryManagementApplication.LibraryManagementApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomExceptions.ResourceNotFoundException.class)
    public ResponseEntity<Response<Object>> handleResourceNotFoundException(
            CustomExceptions.ResourceNotFoundException ex, WebRequest request) {

        Response<Object> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setError("Resource Not Found");
        response.setHttpErrorCode(HttpStatus.NOT_FOUND.value());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(CustomExceptions.BadRequestException.class)
    public ResponseEntity<Response<Object>> handleBadRequestException(
            CustomExceptions.BadRequestException ex) {
        Response<Object> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setError("Bad Request");
        response.setHttpErrorCode(HttpStatus.BAD_REQUEST.value());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExceptions.InternalServerException.class)
    public ResponseEntity<Response<Object>> handleInternalServerException(CustomExceptions.InternalServerException ex) {
        Response<Object> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setError("Internal Server Error");
        response.setHttpErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

