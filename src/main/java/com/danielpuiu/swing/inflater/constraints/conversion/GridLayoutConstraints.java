package com.danielpuiu.swing.inflater.constraints.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.constraints.ConstraintsConversion;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GridLayoutConstraints implements ConstraintsConversion<GridLayout> {

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(GridLayout.class.getName(), GridLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, GridLayout layoutManager, Map<String, String> map) {
        return null;
    }
}
