package com.danielpuiu.swing.util;

public class ObjectUtil {

    private ObjectUtil() {
    }

    public static <T> T cast(Object object) {
        return (T) object;
    }
}
