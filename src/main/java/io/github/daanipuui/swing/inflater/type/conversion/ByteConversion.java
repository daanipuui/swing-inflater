package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

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
