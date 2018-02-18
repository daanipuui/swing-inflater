package com.danielpuiu.swing.type;

import java.util.List;
import java.util.function.Function;

public interface TypeConversion {

    List<String> getHandledTypes();

    Object convert(String value);

    default  <T> T convert(Function<String, T> function, String value) {
        return function.apply(value.trim());
    }
}
