package com.danielpuiu.swing.inflater;

import java.lang.reflect.Field;

public interface Conversion {

    default Object convertConstant(ContextProvider contextProvider, String value) {
        for (String packageName : contextProvider.getPackageNames()) {
            String fullName = packageName + value;

            int dotPosition = fullName.lastIndexOf('.');
            if (dotPosition == -1) {
                continue;
            }

            String className = fullName.substring(0, dotPosition);
            String constantName = fullName.substring(dotPosition + 1);

            try {
                Class<?> cls = Class.forName(className);
                Field field = cls.getDeclaredField(constantName);
                return field.get(null);

            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                // nothing to do
            }
        }

        return null;
    }
}
