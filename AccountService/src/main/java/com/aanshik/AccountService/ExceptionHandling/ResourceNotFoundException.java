package com.aanshik.AccountService.ExceptionHandling;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resourceName, String resource) {
        super(resourceName + " with ID = " + resource + " not found.");
    }
}
