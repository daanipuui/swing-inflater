package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

public class DimensionConversion implements TypeConversion<Dimension> {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Dimension.class.getName(), Dimension.class.getSimpleName(), "dimension");
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

