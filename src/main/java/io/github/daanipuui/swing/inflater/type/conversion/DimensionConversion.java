package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.awt.Dimension;
import java.util.Collections;
import java.util.List;

public class DimensionConversion implements TypeConversion<Dimension> {

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(Dimension.class);
    }

    @Override
    public Dimension convertLiteral(PackageProvider packageProvider, String value) {
        String[] values = value.split(",");
        if (values.length != 2) {
            return null;
        }

        try {
            Dimension dimension = new Dimension();
            dimension.setSize(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            return dimension;
        } catch (Exception e) {
            return null;
        }
    }
}

