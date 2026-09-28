package com.example.restapi.exception;

/** Thrown when the JWT is valid but the referenced user no longer exists. */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
    }
}
