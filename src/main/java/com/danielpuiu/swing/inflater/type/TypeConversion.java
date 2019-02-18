package com.danielpuiu.swing.inflater.type;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.Conversion;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public interface TypeConversion extends Conversion {

    List<String> getHandledTypes();

    default Object convert(ContextProvider contextProvider, String value) {
        Object object = convertConstant(contextProvider, value);
        if (Objects.nonNull(object)) {
            return object;
        }

        return convertLiteral(contextProvider, value);
    }

    Object convertLiteral(ContextProvider contextProvider, String value);

    default <T> T convertLiteral(Function<String, T> function, String value) {
        try {
            return function.apply(value.trim());
        } catch (Exception e) {
            return null;
        }
    }
}
