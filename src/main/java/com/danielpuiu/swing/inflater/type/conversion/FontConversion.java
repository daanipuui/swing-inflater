package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

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
