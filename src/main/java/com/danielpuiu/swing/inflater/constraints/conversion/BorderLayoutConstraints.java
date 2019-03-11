package com.danielpuiu.swing.inflater.constraints.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.constraints.ConstraintsConversion;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;

public class BorderLayoutConstraints implements ConstraintsConversion {

    private static final List<String> POSITIONS = Arrays.asList(CENTER, EAST, WEST, SOUTH, NORTH);

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(BorderLayout.class.getName(), BorderLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, Map<String, String> map) {
        String value = map.getOrDefault("position", CENTER);
        String constraint = (String) convertConstant(contextProvider, "BorderLayout." + value);
        if (Objects.isNull(constraint)) {
            constraint = value;
        }

        return POSITIONS.contains(constraint) ? constraint : CENTER;
    }
}
