package io.github.daanipuui.swing.inflater.type;

import io.github.daanipuui.swing.inflater.Conversion;
import io.github.daanipuui.swing.inflater.PackageProvider;

import java.util.List;
import java.util.function.Function;

public interface TypeConversion<T> extends Conversion {

    List<Class> getHandledTypes();

    T convertLiteral(PackageProvider packageProvider, String value);

    default T convert(PackageProvider packageProvider, String value) {
        T object = convertConstant(packageProvider, value);
        if (object != null) {
            return object;
        }

        return convertLiteral(packageProvider, value);
    }

    default T convertLiteral(Function<String, T> function, String value) {
        try {
            return function.apply(value.trim());
        } catch (Exception e) {
            throw new TypeConversionException(this, value);
        }
    }
}
