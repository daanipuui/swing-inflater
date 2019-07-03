package com.danielpuiu.swing.inflater.type;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.exceptions.NoArgumentConstructorNotFoundException;
import com.danielpuiu.swing.inflater.type.conversion.BooleanConversion;
import com.danielpuiu.swing.inflater.type.conversion.BorderConversion;
import com.danielpuiu.swing.inflater.type.conversion.ByteConversion;
import com.danielpuiu.swing.inflater.type.conversion.CharacterConversion;
import com.danielpuiu.swing.inflater.type.conversion.ColorConversion;
import com.danielpuiu.swing.inflater.type.conversion.ComponentConversion;
import com.danielpuiu.swing.inflater.type.conversion.DimensionConversion;
import com.danielpuiu.swing.inflater.type.conversion.DoubleConversion;
import com.danielpuiu.swing.inflater.type.conversion.EventListenerConversion;
import com.danielpuiu.swing.inflater.type.conversion.FileConversion;
import com.danielpuiu.swing.inflater.type.conversion.FloatConversion;
import com.danielpuiu.swing.inflater.type.conversion.FontConversion;
import com.danielpuiu.swing.inflater.type.conversion.InsetsConversion;
import com.danielpuiu.swing.inflater.type.conversion.IntegerConversion;
import com.danielpuiu.swing.inflater.type.conversion.LayoutConversion;
import com.danielpuiu.swing.inflater.type.conversion.LocaleConversion;
import com.danielpuiu.swing.inflater.type.conversion.LongConversion;
import com.danielpuiu.swing.inflater.type.conversion.RectangleConversion;
import com.danielpuiu.swing.inflater.type.conversion.ShortConversion;
import com.danielpuiu.swing.inflater.type.conversion.StringConversion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.danielpuiu.swing.inflater.util.ObjectUtil.cast;

public class TypeConverter {

    private static final HashMap<Class, TypeConversion> REGISTERED_CONVERTERS = new HashMap<>();

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
                InsetsConversion.class,
                EventListenerConversion.class,
                FloatConversion.class,
                ByteConversion.class,
                LongConversion.class,
                ShortConversion.class,
                ComponentConversion.class,
                FontConversion.class,
                CharacterConversion.class,
                LocaleConversion.class,
                FileConversion.class,
                RectangleConversion.class
        ).forEach(TypeConverter::registerConverter);
    }

    private TypeConverter() {
        // prevent instantiation
    }

    public static <T> T convert(PackageProvider packageProvider, Class type, String value) {
        if (!REGISTERED_CONVERTERS.containsKey(type)) {
            tryRegisterSubType(type);
        }

        return cast(REGISTERED_CONVERTERS.get(type).convert(packageProvider, value));
    }

    @SuppressWarnings("WeakerAccess")
    public static void registerConverter(Class<? extends TypeConversion> typeConversion) {
        registerConverter(newInstance(typeConversion));
    }

    public static void registerConverter(TypeConversion<?> conversion) {
        for (Class type : conversion.getHandledTypes()) {
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
    public static boolean handles(Class type) {
        return REGISTERED_CONVERTERS.containsKey(type);
    }

    public static Object[] convertValues(PackageProvider packageProvider, Class[] types, String[] values) {
        Object[] convertedValues = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            convertedValues[i] = convert(packageProvider, types[i], values[i]);
        }

        return convertedValues;
    }

    private static void tryRegisterSubType(Class type) {
        for (Map.Entry<Class, TypeConversion> entry: REGISTERED_CONVERTERS.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                REGISTERED_CONVERTERS.put(type, entry.getValue());
                return;
            }
        }

        throw new IllegalArgumentException("Unhandled attribute type: " + type);
    }

    private static TypeConversion newInstance(Class<? extends TypeConversion> typeConversion) {
        try {
            return typeConversion.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new NoArgumentConstructorNotFoundException(typeConversion.getName());
        }
    }
}