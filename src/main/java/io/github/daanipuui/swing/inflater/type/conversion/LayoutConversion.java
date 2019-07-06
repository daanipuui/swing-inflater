package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;
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
            Class<LayoutManager> layoutManagerClass = packageProvider.getClass(value);
            if (layoutManagerClass == null) {
                throw new ClassNotFoundException(value);
            }
            return layoutManagerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            logger.error("Layout conversion failed because [{}].", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
