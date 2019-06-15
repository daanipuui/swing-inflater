package com.danielpuiu.swing.inflater.constraints;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.constraints.conversion.BorderLayoutConstraints;
import com.danielpuiu.swing.inflater.constraints.conversion.CardLayoutConstraints;
import com.danielpuiu.swing.inflater.constraints.conversion.FlowLayoutConstraints;
import com.danielpuiu.swing.inflater.constraints.conversion.RelativeLayoutConstraints;
import com.danielpuiu.swing.inflater.exceptions.NoArgumentConstructorNotFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConstraintsConverter {

    private static final HashMap<String, ConstraintsConversion> REGISTERED_CONVERTERS = new HashMap<>();

    static {
        Arrays.asList(RelativeLayoutConstraints.class, FlowLayoutConstraints.class, BorderLayoutConstraints.class, CardLayoutConstraints.class).forEach(
                ConstraintsConverter::registerConverter);
    }

    private ConstraintsConverter() {
        // prevent instantiation
    }

    public static Object convert(ContextProvider contextProvider, String layout, Map<String, String> map) {
        if (REGISTERED_CONVERTERS.containsKey(layout)) {
            return REGISTERED_CONVERTERS.get(layout).convert(contextProvider, map);
        }

        throw new IllegalArgumentException("Unknown layout: " + layout);
    }

    public static void registerConverter(Class<? extends ConstraintsConversion> constraintsConversion) {
        registerConverter(newInstance(constraintsConversion));
    }

    public static void registerConverter(ConstraintsConversion conversion) {
        for (String layout : conversion.getHandledLayouts()) {
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
            throw new NoArgumentConstructorNotFoundException(constraintsConversion.getName());
        }
    }
}
