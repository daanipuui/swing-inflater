package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.LayoutManager;
import java.util.Collections;
import java.util.List;

public class LayoutConversion implements TypeConversion<LayoutManager> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(LayoutManager.class);
    }

    @Override
    public LayoutManager convertLiteral(PackageProvider packageProvider, String value) {
        try {
            return (LayoutManager) getClass(packageProvider, value).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            logger.error("Layout conversion failed because [{}].", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Class<?> getClass(PackageProvider packageProvider, String value) throws ClassNotFoundException {
        for (String packageName : packageProvider.getPackageNames()) {
            try {
                return Class.forName(packageName + value);
            } catch (ClassNotFoundException e) {
                // nothing to do
            }
        }

        throw new ClassNotFoundException(value);
    }
}
