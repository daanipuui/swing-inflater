package com.danielpuiu.swing.inflater.type;

import com.danielpuiu.swing.inflater.Conversion;
import com.danielpuiu.swing.inflater.PackageProvider;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public interface TypeConversion extends Conversion {

    List<String> getHandledTypes();

    default <T> T convert(PackageProvider packageProvider, String value) {
        T object = convertConstant(packageProvider, value);
        if (Objects.nonNull(object)) {
            return object;
        }

        return convertLiteral(packageProvider, value);
    }

    <T> T convertLiteral(PackageProvider packageProvider, String value);

    default <T> T convertLiteral(Function<String, T> function, String value) {
        try {
            return function.apply(value.trim());
        } catch (Exception e) {
            return null;
        }
    }
}
