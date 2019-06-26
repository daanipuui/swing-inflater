package com.danielpuiu.swing.inflater.constraints;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.Conversion;

import java.awt.LayoutManager;
import java.util.List;
import java.util.Map;

public interface ConstraintsConversion<T extends LayoutManager> extends Conversion {

    List<String> getHandledLayouts();

    Object convert(ContextProvider contextProvider, T layoutManager, Map<String, String> map);
}
