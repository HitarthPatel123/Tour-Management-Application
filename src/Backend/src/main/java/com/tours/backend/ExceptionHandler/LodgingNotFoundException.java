package com.tours.backend.ExceptionHandler;

public class LodgingNotFoundException extends RuntimeException {
    public LodgingNotFoundException(String message) {
        super(message);
    }
}
