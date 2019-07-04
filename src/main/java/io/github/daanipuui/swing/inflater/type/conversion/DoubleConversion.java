package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class DoubleConversion implements TypeConversion<Double> {

    @Override
    public List<Class> getHandledTypes() {
        return Arrays.asList(Double.class, double.class);
    }

    @Override
    public Double convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Double::parseDouble, value);
    }
}
