package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class ColorConversion implements TypeConversion<Color> {

    private static final String COLOR_PROPERTY = "swing-inflater.color";

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(Color.class);
    }

    @Override
    public Color convertLiteral(PackageProvider packageProvider, String value) {
        System.setProperty(COLOR_PROPERTY, value);
        return convertLiteral(Color::getColor, COLOR_PROPERTY);
    }
}
