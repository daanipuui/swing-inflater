package com.danielpuiu.swing.type.conversion;

import com.danielpuiu.swing.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class DoubleConversion implements TypeConversion {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Double.class.getName(), Double.class.getSimpleName(), "double");
    }

    @Override
    public Double convert(String value) {
        try {
            return convert(Double::parseDouble, value);
        } catch (Exception e) {
            return null;
        }
    }
}
