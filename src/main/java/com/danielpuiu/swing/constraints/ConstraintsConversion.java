package com.danielpuiu.swing.constraints;

import java.util.List;
import java.util.Map;

public interface ConstraintsConversion {

    List<String> getHandledLayouts();

    Object convert(Map<String, String> map);
}
