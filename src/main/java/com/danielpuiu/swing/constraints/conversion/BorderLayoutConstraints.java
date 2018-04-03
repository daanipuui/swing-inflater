package com.danielpuiu.swing.constraints.conversion;

import com.danielpuiu.swing.constraints.ConstraintsConversion;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public Object convert(Map<String, String> map) {
        String constraint = map.getOrDefault("position", CENTER);
        return POSITIONS.contains(constraint) ? constraint : CENTER;
    }
}
