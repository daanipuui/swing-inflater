package com.danielpuiu.swing.util;

public class MissingDefaultConstructorException extends RuntimeException {

    public MissingDefaultConstructorException(String className) {
        super(className);
    }
}
