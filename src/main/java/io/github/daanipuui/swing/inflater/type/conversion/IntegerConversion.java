package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class IntegerConversion implements TypeConversion<Integer> {

    @Override
    public List<Class> getHandledTypes() {
        return Arrays.asList(Integer.class, int.class);
    }

    @Override
    public Integer convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Integer::parseInt, value);
    }
}
