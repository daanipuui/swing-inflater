package com.danielpuiu.swing.inflater;

import java.lang.reflect.Field;

import static com.danielpuiu.swing.inflater.util.ObjectUtil.cast;

public interface Conversion {

    default <T> T convertConstant(PackageProvider packageProvider, String value) {
        for (String packageName : packageProvider.getPackageNames()) {
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
                return cast(field.get(null));
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                // nothing to do
            }
        }

        return null;
    }
}
