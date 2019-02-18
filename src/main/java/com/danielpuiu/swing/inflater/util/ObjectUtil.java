package com.danielpuiu.swing.inflater.util;

public class ObjectUtil {

    private ObjectUtil() {
        // prevent instantiation
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }
}
