package com.fredyhg.destiny2jobs.exceptions.user;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String msg){
        super(msg);
    }
}
