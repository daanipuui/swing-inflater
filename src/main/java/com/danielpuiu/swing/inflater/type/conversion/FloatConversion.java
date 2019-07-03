package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class FloatConversion implements TypeConversion<Float> {

    @Override
    public List<Class> getHandledTypes() {
        return Arrays.asList(Float.class, float.class);
    }

    @Override
    public Float convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Float::parseFloat, value);
    }
}
