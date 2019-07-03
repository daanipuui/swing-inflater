package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

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
