package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class IntegerConversion implements TypeConversion {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Integer.class.getName(), Integer.class.getSimpleName(), "integer", "int");
    }

    @Override
    public Integer convertLiteral(ContextProvider contextProvider, String value) {
        return convertLiteral(Integer::parseInt, value);
    }
}
