package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class ColorConversion implements TypeConversion {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Color.class.getName(), Color.class.getSimpleName(), "color");
    }

    @Override
    public Object convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Color::getColor, value);
    }
}
