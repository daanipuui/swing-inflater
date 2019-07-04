package io.github.daanipuui.swing.inflater.exceptions;

public class NoArgumentConstructorNotFoundException extends RuntimeException {

    public NoArgumentConstructorNotFoundException(String className) {
        super(className);
    }
}
