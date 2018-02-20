package com.danielpuiu.swing.type;

import com.danielpuiu.swing.type.conversion.*;

import java.util.Arrays;
import java.util.HashMap;

public class TypeConverter {

    private static final HashMap<String, TypeConversion> REGISTERED_CONVERTERS = new HashMap<>();

    static {
        Arrays.asList(
                IntegerConversion.class,
                DoubleConversion.class,
                BooleanConversion.class,
                StringConversion.class,
                ColorConversion.class
        ).forEach(TypeConverter::registerConverter);
    }

    public static Object convert(String type, String value) {
        if (REGISTERED_CONVERTERS.containsKey(type)) {
            return REGISTERED_CONVERTERS.get(type).convert(value);
        }

        throw new IllegalArgumentException("Unregistered type: " + type);
    }

    public static void registerConverter(Class<? extends TypeConversion> typeConversion) {
        registerConverter(newInstance(typeConversion));
    }

    public static void registerConverter(TypeConversion conversion) {
        for (String type: conversion.getHandledTypes()) {
            REGISTERED_CONVERTERS.put(type, conversion);
        }
    }

    public static void unregisterConverter(Class<? extends TypeConversion> typeConversion) {
        newInstance(typeConversion).getHandledTypes().forEach(REGISTERED_CONVERTERS::remove);
    }

    public static boolean handles(String type) {
        return REGISTERED_CONVERTERS.containsKey(type);
    }

    private static TypeConversion newInstance(Class<? extends TypeConversion> typeConversion) {
        try {
            return typeConversion.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MissingDefaultConstructorException(typeConversion.getName());
        }
    }
}
