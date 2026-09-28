package com.example.restapi.exception;

/** Thrown when login credentials do not match any known user. */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid userId or password");
    }
}
