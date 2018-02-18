package com.danielpuiu.swing.type.conversion;

import com.danielpuiu.swing.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class BooleanConversion implements TypeConversion {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Boolean.class.getName(), Boolean.class.getSimpleName(), "boolean");
    }

    @Override
    public Object convert(String value) {
        try {
            return convert(Boolean::parseBoolean, value);
        } catch (Exception e) {
            return null;
        }
    }
}
