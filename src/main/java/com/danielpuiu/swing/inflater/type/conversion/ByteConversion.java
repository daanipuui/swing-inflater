package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class ByteConversion implements TypeConversion<Byte> {

    @Override
    public List<Class> getHandledTypes() {
        return Arrays.asList(Byte.class, byte.class);
    }

    @Override
    public Byte convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Byte::parseByte, value);
    }
}
