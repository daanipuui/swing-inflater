package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RectangleConversion implements TypeConversion<Rectangle> {

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(Rectangle.class);
    }

    @Override
    public Rectangle convertLiteral(PackageProvider packageProvider, String value) {
        int[] values = Arrays.stream(value.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();

        switch (values.length) {
            case 0:
                return new Rectangle();
            case 2:
                return new Rectangle(values[0], values[1]);
            case 4:
                return new Rectangle(values[0], values[1], values[2], values[3]);
            default:
                String errorMessage = String.format("Cannot convert [%s] to rectangle.", value);
                throw new IllegalArgumentException(errorMessage);
        }
    }
}
