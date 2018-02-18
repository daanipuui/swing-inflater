package com.danielpuiu.swing.type;

public class MissingDefaultConstructorException extends RuntimeException {

    MissingDefaultConstructorException(String className) {
        super(className);
    }
}
