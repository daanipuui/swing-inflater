package com.danielpuiu.swing.inflater.constraints.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.constraints.ConstraintsConversion;
import com.danielpuiu.swing.inflater.type.TypeConverter;

import javax.swing.Spring;
import javax.swing.SpringLayout;
import java.awt.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.HORIZONTAL_CENTER;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.VERTICAL_CENTER;
import static javax.swing.SpringLayout.WEST;

public class SprintLayoutConstraints implements ConstraintsConversion<SpringLayout> {

    private static final String VALUE_DELIMITER = ",";

    private final Map<String, String[]> attributeTypeToSpringEdges = init();

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(SpringLayout.class.getName(), SpringLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, SpringLayout layoutManager, Map<String, String> map) {
        SpringLayout.Constraints constraints = new SpringLayout.Constraints();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String attribute = entry.getKey();
            String[] edges = attributeTypeToSpringEdges.get(attribute);
            if (Objects.isNull(edges)) {
                throw new IllegalArgumentException(String.format("Unknown attribute [%s].", attribute));
            }

            String[] values = entry.getValue().split(VALUE_DELIMITER);
            if (values.length > 2) {
                throw new IllegalArgumentException(String.format("[%s] contains [%d] arguments. Required at most 2.", entry.getValue(), values.length));
            }

            Component component = TypeConverter.convert(contextProvider, Component.class.getSimpleName(), values[0].trim());
            int pad = values.length == 2? Integer.parseInt(values[1].trim()): 0;

            Spring spring = Spring.sum(Spring.constant(pad), layoutManager.getConstraint(edges[1], component));
            constraints.setConstraint(edges[0], spring);
        }

        return constraints;
    }

    private Map<String, String[]> init() {
        Map<String, String[]> map = new HashMap<>();

        map.put("above", new String[]{SOUTH, NORTH});
        map.put("alignBottom", new String[]{SOUTH, SOUTH});
        map.put("alignLeft", new String[]{WEST, WEST});
        map.put("alignRight", new String[]{EAST, EAST});
        map.put("alignTop", new String[]{NORTH, NORTH});
        map.put("below", new String[]{NORTH, SOUTH});
        map.put("centerHorizontal", new String[]{HORIZONTAL_CENTER, HORIZONTAL_CENTER});
        map.put("centerVertical", new String[]{VERTICAL_CENTER, VERTICAL_CENTER});
        map.put("leftOf", new String[]{EAST, WEST});
        map.put("rightOf", new String[]{WEST, EAST});

        return map;
    }
}
