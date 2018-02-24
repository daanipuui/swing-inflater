package com.danielpuiu.swing.type.conversion;

import com.danielpuiu.swing.type.TypeConversion;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class ColorConversion implements TypeConversion {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Color.class.getName(), Color.class.getSimpleName(), "color");
    }

    @Override
    public Object convert(String value) {
        try {
            return convert(Color::decode, value);
        } catch (Exception e) {
            return null;
        }
    }
}
