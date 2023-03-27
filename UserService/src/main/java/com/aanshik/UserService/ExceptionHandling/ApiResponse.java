package com.aanshik.UserService.ExceptionHandling;

public class ApiResponse {
    String error;

    public ApiResponse(String message) {
        this.error = message;
    }
}
