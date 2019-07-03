package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleConversion implements TypeConversion<Locale> {

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(Locale.class);
    }

    @Override
    public Locale convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(Locale::forLanguageTag, value);
    }
}
