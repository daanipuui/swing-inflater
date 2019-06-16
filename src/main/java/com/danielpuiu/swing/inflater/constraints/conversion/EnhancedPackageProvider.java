package com.danielpuiu.swing.inflater.constraints.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;

import java.util.HashSet;
import java.util.Set;

class EnhancedPackageProvider implements PackageProvider {

    private Set<String> packageNames;

    EnhancedPackageProvider(PackageProvider packageProvider) {
        packageNames = new HashSet<>(packageProvider.getPackageNames());
    }

    @Override
    public Set<String> getPackageNames() {
        return packageNames;
    }

    void addClass(Class<?> cls) {
        if (packageNames.contains(cls.getPackage().getName() + ".")) {
            packageNames.add(cls.getName() + ".");
        }
    }
}
