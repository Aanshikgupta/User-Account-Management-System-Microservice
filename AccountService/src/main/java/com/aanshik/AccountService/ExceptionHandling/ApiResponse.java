package com.aanshik.AccountService.ExceptionHandling;

public class ApiResponse {
    String error;

    public ApiResponse(String message) {
        this.error = message;
    }
}
