package com.fredyhg.destiny2jobs.exceptions.user;

public class WorkerNotAllowedException extends RuntimeException{
    public WorkerNotAllowedException(String msg){
        super(msg);
    }
}
