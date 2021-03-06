package io.github.daanipuui.swing.inflater.constraints;

import io.github.daanipuui.swing.inflater.ContextProvider;
import io.github.daanipuui.swing.inflater.constraints.conversion.BorderLayoutConstraints;
import io.github.daanipuui.swing.inflater.constraints.conversion.CardLayoutConstraints;
import io.github.daanipuui.swing.inflater.constraints.conversion.FlowLayoutConstraints;
import io.github.daanipuui.swing.inflater.constraints.conversion.GridBagLayoutConstraints;
import io.github.daanipuui.swing.inflater.constraints.conversion.GridLayoutConstraints;
import io.github.daanipuui.swing.inflater.constraints.conversion.RelativeLayoutConstraints;
import io.github.daanipuui.swing.inflater.constraints.conversion.SprintLayoutConstraints;
import io.github.daanipuui.swing.inflater.exceptions.NoArgumentConstructorNotFoundException;

import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConstraintsConverter {

    private static final Map<String, ConstraintsConversion<LayoutManager>> REGISTERED_CONVERTERS = new HashMap<>();

    static {
        Arrays.asList(
                RelativeLayoutConstraints.class,
                FlowLayoutConstraints.class,
                BorderLayoutConstraints.class,
                CardLayoutConstraints.class,
                GridBagLayoutConstraints.class,
                GridLayoutConstraints.class,
                SprintLayoutConstraints.class
        ).forEach(ConstraintsConverter::registerConverter);
    }

    private ConstraintsConverter() {
        // prevent instantiation
    }

    public static Object convert(ContextProvider contextProvider, LayoutManager layoutManager, Map<String, String> map) {
        String className = layoutManager.getClass().getName();
        if (REGISTERED_CONVERTERS.containsKey(className)) {
            return REGISTERED_CONVERTERS.get(className).convert(contextProvider, layoutManager, map);
        }

        throw new IllegalArgumentException("Unhandled layout type: " + className);
    }

    @SuppressWarnings("WeakerAccess")
    public static void registerConverter(Class<? extends ConstraintsConversion> constraintsConversion) {
        registerConverter(newInstance(constraintsConversion));
    }

    @SuppressWarnings("WeakerAccess")
    public static void registerConverter(ConstraintsConversion<LayoutManager> conversion) {
        for (String layout : conversion.getHandledLayouts()) {
            REGISTERED_CONVERTERS.put(layout, conversion);
        }
    }

    @SuppressWarnings("unused")
    public static void unregisterConverter(Class<? extends ConstraintsConversion> constraintsConversion) {
        newInstance(constraintsConversion).getHandledLayouts().forEach(REGISTERED_CONVERTERS::remove);
    }

    @SuppressWarnings("unused")
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
