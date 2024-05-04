package com.park.api.exception;

public class CpfUniqueViolationException extends Throwable {

    public CpfUniqueViolationException(String msg){
        super(msg);
    }
}
