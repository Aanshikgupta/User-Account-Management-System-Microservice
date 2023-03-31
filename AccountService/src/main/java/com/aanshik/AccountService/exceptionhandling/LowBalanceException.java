package com.aanshik.AccountService.exceptionhandling;

public class LowBalanceException extends RuntimeException{

    public LowBalanceException() {
        super("Insufficient Balance");
    }
}
