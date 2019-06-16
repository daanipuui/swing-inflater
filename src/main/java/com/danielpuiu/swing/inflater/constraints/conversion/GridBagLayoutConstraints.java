package com.danielpuiu.swing.inflater.constraints.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.constraints.ConstraintsConversion;
import com.danielpuiu.swing.inflater.type.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridBagLayoutConstraints implements ConstraintsConversion {

    private static final Map<String, String> attributeToTypeNames = init();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(GridBagLayout.class.getName(), GridBagLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, Map<String, String> map) {
        EnhancedPackageProvider packageProvider = new EnhancedPackageProvider(contextProvider);
        packageProvider.addClass(GridBagConstraints.class);

        GridBagConstraints constraints = new GridBagConstraints();

        for (String attributeName: attributeToTypeNames.keySet()) {
            if (map.containsKey(attributeName)) {
                addConstraint(constraints, attributeName, packageProvider, map);
            }
        }

        return constraints;
    }

    private void addConstraint(GridBagConstraints constraints, String attribute, PackageProvider packageProvider, Map<String, String> map) {
        try {
            Field field = constraints.getClass().getField(attribute);
            Object value = TypeConverter.convert(packageProvider, attributeToTypeNames.get(attribute), map.get(attribute));

            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(constraints, value);
            field.setAccessible(accessible);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Cannot add constraint for attribute [{}] because [{}].", attribute, e.getMessage());
        }
    }

    private static Map<String, String> init() {
        Map<String, String> map = new HashMap<>();

        map.put("fill", "int");
        map.put("gridx", "int");
        map.put("gridy", "int");
        map.put("gridwidth", "int");
        map.put("gridheight", "int");
        map.put("ipadx", "int");
        map.put("ipady", "int");
        map.put("anchor", "int");
        map.put("insets", "insets");
        map.put("weightx", "double");
        map.put("weighty", "double");

        return Collections.unmodifiableMap(map);
    }
}