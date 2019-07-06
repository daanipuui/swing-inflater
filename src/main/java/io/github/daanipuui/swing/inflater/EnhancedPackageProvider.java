package io.github.daanipuui.swing.inflater;

import java.util.HashSet;
import java.util.Set;

public class EnhancedPackageProvider implements PackageProvider {

    private Set<String> packageNames;

    public EnhancedPackageProvider(PackageProvider packageProvider) {
        packageNames = new HashSet<>(packageProvider.getPackageNames());
    }

    @Override
    public Set<String> getPackageNames() {
        return packageNames;
    }

    public void addClass(Class<?> cls) {
        if (packageNames.contains(cls.getPackage().getName() + ".")) {
            packageNames.add(cls.getName() + ".");
        }
    }
}
