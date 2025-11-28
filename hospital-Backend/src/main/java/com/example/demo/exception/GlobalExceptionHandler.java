package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.entity.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

 
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "User not found in system");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDoctorNotFound(DoctorNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Doctor not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoctorDeletionException.class)
    public ResponseEntity<ErrorResponse> handleDoctorDeletion(DoctorDeletionException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Doctor deletion failed");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

   
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Requested resource not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(NoDataFoundInDatabaseException.class)
    public ResponseEntity<ErrorResponse> handleNoDataFound(NoDataFoundInDatabaseException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "No data available in database");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

   
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Internal server error");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
