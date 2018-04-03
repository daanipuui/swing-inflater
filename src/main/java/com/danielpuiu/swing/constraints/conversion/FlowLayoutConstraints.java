package com.danielpuiu.swing.constraints.conversion;

import com.danielpuiu.swing.constraints.ConstraintsConversion;

import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FlowLayoutConstraints implements ConstraintsConversion {

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(FlowLayout.class.getName(), FlowLayout.class.getSimpleName());
    }

    @Override
    public Object convert(Map<String, String> map) {
        return null;
    }
}
