package com.danielpuiu.swing.constraints;

import com.danielpuiu.swing.constraints.conversion.RelativeLayoutConstraints;
import com.danielpuiu.swing.util.MissingDefaultConstructorException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConstraintsConverter {

    private static final HashMap<String, ConstraintsConversion> REGISTERED_CONVERTERS = new HashMap<>();

    static {
        Arrays.asList(
            RelativeLayoutConstraints.class
        ).forEach(ConstraintsConverter::registerConverter);
    }

    private ConstraintsConverter() {
    }

    public static Object convert(String layout, Map<String, String> map) {
        if (REGISTERED_CONVERTERS.containsKey(layout)) {
            return REGISTERED_CONVERTERS.get(layout).convert(map);
        }

        throw new IllegalArgumentException("Unknown layout: " + layout);
    }

    public static void registerConverter(Class<? extends ConstraintsConversion> constraintsConversion) {
        registerConverter(newInstance(constraintsConversion));
    }

    public static void registerConverter(ConstraintsConversion conversion) {
        for (String layout: conversion.getHandledLayouts()) {
            REGISTERED_CONVERTERS.put(layout, conversion);
        }
    }

    public static void unregisterConverter(Class<? extends ConstraintsConversion> constraintsConversion) {
        newInstance(constraintsConversion).getHandledLayouts().forEach(REGISTERED_CONVERTERS::remove);
    }

    public static boolean handles(String layout) {
        return REGISTERED_CONVERTERS.containsKey(layout);
    }

    private static ConstraintsConversion newInstance(Class<? extends ConstraintsConversion> constraintsConversion) {
        try {
            return constraintsConversion.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MissingDefaultConstructorException(constraintsConversion.getName());
        }
    }
}
