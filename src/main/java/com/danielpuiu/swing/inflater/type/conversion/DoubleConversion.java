package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class DoubleConversion implements TypeConversion {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Double.class.getName(), Double.class.getSimpleName(), "double");
    }

    @Override
    public Double convertLiteral(ContextProvider contextProvider, String value) {
        return convertLiteral(Double::parseDouble, value);
    }
}
