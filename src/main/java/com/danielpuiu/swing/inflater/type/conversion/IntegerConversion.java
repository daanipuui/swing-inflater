package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class IntegerConversion implements TypeConversion {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Integer.class.getName(), Integer.class.getSimpleName(), "integer", "int");
    }

    @Override
    public Integer convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Integer::parseInt, value);
    }
}
