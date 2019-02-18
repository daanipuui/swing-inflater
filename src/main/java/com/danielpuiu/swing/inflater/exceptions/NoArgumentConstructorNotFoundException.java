package com.danielpuiu.swing.inflater.exceptions;

public class NoArgumentConstructorNotFoundException extends RuntimeException {

    public NoArgumentConstructorNotFoundException(String className) {
        super(className);
    }
}
