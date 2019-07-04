package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class BooleanConversion implements TypeConversion<Boolean> {

    @Override
    public List<Class> getHandledTypes() {
        return Arrays.asList(Boolean.class, boolean.class);
    }

    @Override
    public Boolean convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Boolean::parseBoolean, value);
    }
}
