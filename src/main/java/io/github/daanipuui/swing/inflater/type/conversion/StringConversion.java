package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.util.Collections;
import java.util.List;

public class StringConversion implements TypeConversion<String> {

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(String.class);
    }

    @Override
    public String convertLiteral(PackageProvider packageProvider, String value) {
        return value;
    }
}
