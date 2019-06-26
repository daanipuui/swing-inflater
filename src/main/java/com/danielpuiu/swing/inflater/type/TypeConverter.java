package com.danielpuiu.swing.inflater.type;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.exceptions.NoArgumentConstructorNotFoundException;
import com.danielpuiu.swing.inflater.type.conversion.BooleanConversion;
import com.danielpuiu.swing.inflater.type.conversion.BorderConversion;
import com.danielpuiu.swing.inflater.type.conversion.ColorConversion;
import com.danielpuiu.swing.inflater.type.conversion.DimensionConversion;
import com.danielpuiu.swing.inflater.type.conversion.DoubleConversion;
import com.danielpuiu.swing.inflater.type.conversion.InsetsConversion;
import com.danielpuiu.swing.inflater.type.conversion.IntegerConversion;
import com.danielpuiu.swing.inflater.type.conversion.LayoutConversion;
import com.danielpuiu.swing.inflater.type.conversion.StringConversion;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;

import static com.danielpuiu.swing.inflater.util.ObjectUtil.cast;

public class TypeConverter {

    private static final HashMap<String, TypeConversion> REGISTERED_CONVERTERS = new HashMap<>();

    static {
        Arrays.asList(
                IntegerConversion.class,
                DoubleConversion.class,
                BooleanConversion.class,
                StringConversion.class,
                ColorConversion.class,
                LayoutConversion.class,
                DimensionConversion.class,
                BorderConversion.class,
                InsetsConversion.class
        ).forEach(TypeConverter::registerConverter);
    }

    private TypeConverter() {
        // prevent instantiation
    }

    public static <T> T convert(PackageProvider packageProvider, String type, String value) {
        if (REGISTERED_CONVERTERS.containsKey(type)) {
            return cast(REGISTERED_CONVERTERS.get(type).convert(packageProvider, value));
        }

        throw new IllegalArgumentException("Unknown type: " + type);
    }

    @SuppressWarnings("WeakerAccess")
    public static void registerConverter(Class<? extends TypeConversion> typeConversion) {
        registerConverter(newInstance(typeConversion));
    }

    public static void registerConverter(TypeConversion<?> conversion) {
        for (String type : conversion.getHandledTypes()) {
            REGISTERED_CONVERTERS.put(type, conversion);
        }
    }

    @SuppressWarnings("unused")
    public static void unregisterConverter(Class<? extends TypeConversion> typeConversion) {
        newInstance(typeConversion).getHandledTypes().forEach(REGISTERED_CONVERTERS::remove);
    }

    @SuppressWarnings("unused")
    public static void unregisterConverter(TypeConversion conversion) {
        conversion.getHandledTypes().forEach(REGISTERED_CONVERTERS::remove);
    }

    @SuppressWarnings("unused")
    public static boolean handles(String type) {
        return REGISTERED_CONVERTERS.containsKey(type);
    }

    public static Object[] convertValues(PackageProvider packageProvider, Type[] types, String[] values) {
        Object[] convertedValues = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            convertedValues[i] = convert(packageProvider, types[i].getTypeName(), values[i]);
        }

        return convertedValues;
    }

    private static TypeConversion newInstance(Class<? extends TypeConversion> typeConversion) {
        try {
            return typeConversion.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new NoArgumentConstructorNotFoundException(typeConversion.getName());
        }
    }
}