package com.danielpuiu.swing.inflater.constraints.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.constraints.ConstraintsConversion;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.awt.BorderLayout.CENTER;

public class BorderLayoutConstraints implements ConstraintsConversion {

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(BorderLayout.class.getName(), BorderLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, Map<String, String> map) {
        String value = map.getOrDefault("position", CENTER);
        return convertConstant(contextProvider, "BorderLayout." + value);
    }
}
