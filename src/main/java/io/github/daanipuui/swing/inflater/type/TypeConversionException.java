package io.github.daanipuui.swing.inflater.type;

public class TypeConversionException extends RuntimeException {

    public TypeConversionException(TypeConversion typeConversion, String value) {
        super(String.format("Cannot convert value [%s] using type converter [%s]", value, typeConversion));
    }
}
