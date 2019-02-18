package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.util.Arrays;
import java.util.List;

public class LayoutConversion implements TypeConversion {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(LayoutManager.class.getName(), LayoutManager.class.getSimpleName(), LayoutManager2.class.getName(),
                LayoutManager2.class.getSimpleName());
    }

    @Override
    public Object convertLiteral(ContextProvider contextProvider, String value) {
        try {
            return getClass(contextProvider, value).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            logger.error("Layout conversion failed because [{}].", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Class<?> getClass(ContextProvider contextProvider, String value) throws ClassNotFoundException {
        for (String packageName : contextProvider.getPackageNames()) {
            try {
                return Class.forName(packageName + value);
            } catch (ClassNotFoundException e) {
                // nothing to do
            }
        }

        throw new ClassNotFoundException(value);
    }
}
