package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class ShortConversion implements TypeConversion<Short> {

    @Override
    public List<Class> getHandledTypes() {
        return Arrays.asList(Short.class, short.class);
    }

    @Override
    public Short convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Short::parseShort, value);
    }
}
