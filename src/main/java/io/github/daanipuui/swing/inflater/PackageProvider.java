package io.github.daanipuui.swing.inflater;

import java.util.Set;

import static io.github.daanipuui.swing.inflater.util.ObjectUtil.cast;

public interface PackageProvider {

    Set<String> getPackageNames();

    default <T> Class<T> getClass(String className) {
        if (PrimitiveClasses.isPrimitiveClass(className)) {
            return PrimitiveClasses.getClass(className);
        }

        for (String packageName: getPackageNames()) {
            try {
                return cast(Class.forName(packageName + className));
            } catch (ClassNotFoundException e) {
                // nothing to do
            }
        }

        return null;
    }
}
