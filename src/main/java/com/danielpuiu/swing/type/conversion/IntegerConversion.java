package com.danielpuiu.swing.type.conversion;

import com.danielpuiu.swing.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class IntegerConversion implements TypeConversion {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Integer.class.getName(), Integer.class.getSimpleName(), "integer", "int");
    }

    @Override
    public Integer convert(String value) {
        try {
            return convert(Integer::parseInt, value);
        } catch (Exception e) {
            return null;
        }
    }
}
