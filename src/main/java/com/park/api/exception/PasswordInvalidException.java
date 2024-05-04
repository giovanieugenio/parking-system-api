package com.park.api.exception;

public class PasswordInvalidException extends Throwable{

    public PasswordInvalidException(String msg){
        super(msg);
    }
}
