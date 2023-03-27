package com.aanshik.AccountService.ExceptionHandling;

public class LowBalanceException extends RuntimeException{

    public LowBalanceException() {
        super("Insufficient Balance");
    }
}
