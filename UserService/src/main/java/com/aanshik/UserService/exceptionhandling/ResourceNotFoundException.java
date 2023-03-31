package com.aanshik.UserService.exceptionhandling;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resourceName, String resource) {
        super(resourceName + " with ID = " + resource + " not found.");
    }
}
