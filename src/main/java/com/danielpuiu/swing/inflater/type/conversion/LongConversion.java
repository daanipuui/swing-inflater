package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class LongConversion implements TypeConversion<Long> {

    @Override
    public List<Class> getHandledTypes() {
        return Arrays.asList(Long.class, long.class);
    }

    @Override
    public Long convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Long::parseLong, value);
    }
}
