package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

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
