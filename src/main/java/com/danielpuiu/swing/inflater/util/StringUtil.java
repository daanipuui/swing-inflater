package com.danielpuiu.swing.inflater.util;

import java.util.Objects;

public class StringUtil {

    private static final int FIRST_CHARACTER = 0;
    private static final int SECOND_CHARACTER = 1;

    private StringUtil() {
        // prevent instantiation
    }

    public static String capitalize(String value) {
        if (Objects.isNull(value) || value.isEmpty()) {
            return value;
        }

        if (value.length() == 1) {
            return value.toUpperCase();
        }

        return value.substring(FIRST_CHARACTER, SECOND_CHARACTER).toUpperCase() + value.substring(SECOND_CHARACTER);
    }
}
