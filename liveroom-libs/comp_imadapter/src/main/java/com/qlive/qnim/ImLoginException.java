package com.qlive.qnim;

public class ImLoginException extends Throwable{
    public int code;
    public ImLoginException(int code , String msg){
        super(msg);
    }
}
