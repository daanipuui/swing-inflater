package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class BooleanConversion implements TypeConversion {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Boolean.class.getName(), Boolean.class.getSimpleName(), "boolean");
    }

    @Override
    public Object convertLiteral(ContextProvider contextProvider, String value) {
        return convertLiteral(Boolean::parseBoolean, value);
    }
}
