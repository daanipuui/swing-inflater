package io.github.daanipuui.swing.inflater.constraints.conversion;

import io.github.daanipuui.swing.inflater.ContextProvider;
import io.github.daanipuui.swing.inflater.EnhancedPackageProvider;
import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.constraints.ConstraintsConversion;
import io.github.daanipuui.swing.inflater.type.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridBagLayoutConstraints implements ConstraintsConversion<GridBagLayout> {

    private static final Map<String, Class> attributeToTypes = init();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(GridBagLayout.class.getName(), GridBagLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, GridBagLayout layoutManager, Map<String, String> map) {
        EnhancedPackageProvider packageProvider = new EnhancedPackageProvider(contextProvider);
        packageProvider.addClass(GridBagConstraints.class);

        GridBagConstraints constraints = new GridBagConstraints();

        for (String attributeName: attributeToTypes.keySet()) {
            if (map.containsKey(attributeName)) {
                addConstraint(constraints, attributeName, packageProvider, map);
            }
        }

        return constraints;
    }

    private void addConstraint(GridBagConstraints constraints, String attribute, PackageProvider packageProvider, Map<String, String> map) {
        try {
            Field field = constraints.getClass().getField(attribute);
            Object value = TypeConverter.convert(packageProvider, attributeToTypes.get(attribute), map.get(attribute));

            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(constraints, value);
            field.setAccessible(accessible);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Cannot add constraint for attribute [{}] because [{}].", attribute, e.getMessage());
        }
    }

    private static Map<String, Class> init() {
        Map<String, Class> map = new HashMap<>();

        map.put("fill", int.class);
        map.put("gridx", int.class);
        map.put("gridy", int.class);
        map.put("gridwidth", int.class);
        map.put("gridheight", int.class);
        map.put("ipadx", int.class);
        map.put("ipady", int.class);
        map.put("anchor", int.class);
        map.put("insets", Insets.class);
        map.put("weightx", double.class);
        map.put("weighty", double.class);

        return Collections.unmodifiableMap(map);
    }
}
