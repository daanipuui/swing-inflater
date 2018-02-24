package com.danielpuiu.swing.type;

import com.danielpuiu.swing.type.conversion.BooleanConversion;
import com.danielpuiu.swing.type.conversion.ColorConversion;
import com.danielpuiu.swing.type.conversion.DoubleConversion;
import com.danielpuiu.swing.type.conversion.IntegerConversion;
import com.danielpuiu.swing.type.conversion.LayoutConversion;
import com.danielpuiu.swing.type.conversion.StringConversion;
import com.danielpuiu.swing.util.MissingDefaultConstructorException;

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
                ColorConversion.class,
                LayoutConversion.class
        ).forEach(TypeConverter::registerConverter);
    }

    private TypeConverter() {
    }

    public static Object convert(String type, String value) {
        if (REGISTERED_CONVERTERS.containsKey(type)) {
            return REGISTERED_CONVERTERS.get(type).convert(value);
        }

        throw new IllegalArgumentException("Unknown type: " + type);
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

    public static void unregisterConverter(TypeConversion conversion) {
        conversion.getHandledTypes().forEach(REGISTERED_CONVERTERS::remove);
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
