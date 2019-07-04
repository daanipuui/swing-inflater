package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.awt.Component;
import java.util.Collections;
import java.util.List;

public class ComponentConversion implements TypeConversion<Component> {

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(Component.class);
    }

    @Override
    public Component convertLiteral(PackageProvider packageProvider, String value) {
        return convert(packageProvider, value);
    }
}
