package com.fredyhg.destiny2jobs.exceptions.user;

public class UserNotAllowedException extends RuntimeException{
    public UserNotAllowedException(String msg){
        super(msg);
    }
}
