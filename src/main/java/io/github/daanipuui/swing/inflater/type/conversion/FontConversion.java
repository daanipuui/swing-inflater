package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.awt.Font;
import java.util.Collections;
import java.util.List;

public class FontConversion implements TypeConversion<Font> {

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(Font.class);
    }

    @Override
    public Font convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Font::getFont, value);
    }
}
