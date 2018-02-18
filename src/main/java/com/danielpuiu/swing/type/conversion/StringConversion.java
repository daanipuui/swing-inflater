package com.danielpuiu.swing.type.conversion;

import com.danielpuiu.swing.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class StringConversion implements TypeConversion {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(String.class.getName(), String.class.getSimpleName(), "string");
    }

    @Override
    public Object convert(String value) {
        return value;
    }
}
