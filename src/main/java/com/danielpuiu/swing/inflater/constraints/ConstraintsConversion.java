package com.danielpuiu.swing.inflater.constraints;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.Conversion;

import java.util.List;
import java.util.Map;

public interface ConstraintsConversion extends Conversion {

    List<String> getHandledLayouts();

    Object convert(ContextProvider contextProvider, Map<String, String> map);
}
