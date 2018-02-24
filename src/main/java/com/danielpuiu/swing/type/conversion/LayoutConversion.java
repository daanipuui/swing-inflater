package com.danielpuiu.swing.type.conversion;

import com.danielpuiu.swing.type.TypeConversion;
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
        return Arrays.asList(LayoutManager.class.getName(), LayoutManager.class.getSimpleName(),
                LayoutManager2.class.getName(), LayoutManager2.class.getSimpleName());
    }

    @Override
    public Object convert(String value) {
        try {
            return Class.forName(value).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
