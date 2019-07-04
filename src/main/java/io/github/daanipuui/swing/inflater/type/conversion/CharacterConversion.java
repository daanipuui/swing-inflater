package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.util.Arrays;
import java.util.List;

public class CharacterConversion implements TypeConversion<Character> {

    @Override
    public List<Class> getHandledTypes() {
        return Arrays.asList(Character.class, byte.class);
    }

    @Override
    public Character convertLiteral(PackageProvider packageProvider, String value) {
        if (value.length() != 1) {
            String errorMessage = String.format("[%s] must have exactly one character. It has [%d] characters.", value, value.length());
            throw new IllegalArgumentException(errorMessage);
        }

        return value.charAt(0);
    }
}
