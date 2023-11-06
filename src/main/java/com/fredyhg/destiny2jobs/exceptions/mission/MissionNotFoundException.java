package com.fredyhg.destiny2jobs.exceptions.mission;

public class MissionNotFoundException extends RuntimeException{
    public MissionNotFoundException(String msg){
        super(msg);
    }
}
