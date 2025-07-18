package com.tours.backend.ExceptionHandler;

public class InvalidTourDataException extends RuntimeException {
    public InvalidTourDataException(String message) {
        super(message);
    }
}
