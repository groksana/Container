package com.gromoks.container.exception;

public class BeanInstantiationException extends RuntimeException{
    public BeanInstantiationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}